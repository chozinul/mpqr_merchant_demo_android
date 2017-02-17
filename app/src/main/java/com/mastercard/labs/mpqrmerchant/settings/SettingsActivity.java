package com.mastercard.labs.mpqrmerchant.settings;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AlertDialogLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mastercard.labs.mpqrmerchant.R;
import com.mastercard.labs.mpqrmerchant.adapter.SettingsAdapter;
import com.mastercard.labs.mpqrmerchant.data.RealmDataSource;
import com.mastercard.labs.mpqrmerchant.data.model.Settings;
import com.mastercard.labs.mpqrmerchant.utils.DialogUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity implements SettingsContract.View, SettingsAdapter.SettingsItemListener {
    public static String BUNDLE_USER_KEY = "userId";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.img_merchant_logo)
    ImageView mMerchantLogoImageView;

    @BindView(R.id.rv_settings)
    RecyclerView mSettingsRecyclerView;

    private long mUserId;
    private SettingsContract.Presenter mPresenter;
    private SettingsAdapter mSettingsAdapter;

    public static Intent newIntent(Context context, long userId) {
        Intent intent = new Intent(context, SettingsActivity.class);

        intent.putExtra(BUNDLE_USER_KEY, userId);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSettingsRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mSettingsRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        mSettingsRecyclerView.addItemDecoration(dividerItemDecoration);

        if (savedInstanceState != null) {
            mUserId = savedInstanceState.getLong(BUNDLE_USER_KEY, -1);
        } else {
            mUserId = getIntent().getLongExtra(BUNDLE_USER_KEY, -1);
        }

        mPresenter = new SettingsPresenter(this, RealmDataSource.getInstance(), mUserId);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mPresenter.start();
    }

    @Override
    public void showUserNotFound() {
        DialogUtils.customAlertDialogBuilder(this, R.string.error_user_not_found)
                .setCancelable(false)
                .setNegativeButton(R.string.logout, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                }).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong(BUNDLE_USER_KEY, mUserId);

        super.onSaveInstanceState(outState);
    }

    @Override
    public String getMerchantNameTitle() {
        return getString(R.string.merchant_name);
    }

    @Override
    public String getCountryTitle() {
        return getString(R.string.country);
    }

    @Override
    public String getCityTitle() {
        return getString(R.string.city);
    }

    @Override
    public String getMerchantCategoryCodeTitle() {
        return getString(R.string.merchant_category_code);
    }

    @Override
    public String getCardNumberTitle() {
        return getString(R.string.card_number);
    }

    @Override
    public String getCurrencyTitle() {
        return getString(R.string.currency);
    }

    @Override
    public String getStoreIdTitle() {
        return getString(R.string.store_id);
    }

    @Override
    public String getTerminalIdTitle() {
        return getString(R.string.terminal_no);
    }

    @Override
    public void showSettings(List<Settings> allSettings) {
        mSettingsAdapter = new SettingsAdapter(allSettings, this);
        mSettingsRecyclerView.setAdapter(mSettingsAdapter);
    }

    @Override
    public void showMerchantNameEditor(String name) {
        LinearLayout layout = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(params);

        int padding = getResources().getDimensionPixelSize(R.dimen.size_14);
        layout.setPadding(padding, 0, padding, 0);

        final EditText input = new EditText(layout.getContext());
        input.setText(name);
        input.setSelection(name.length(), name.length());

        layout.addView(input, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.merchant_name)
                .setMessage(R.string.enter_merchant_name)
                .setView(layout)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mPresenter.merchantNameUpdated(input.getText().toString());
                    }
                }).create();

        Window window = dialog.getWindow();
        if (window != null) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }

        dialog.show();
    }

    @Override
    public void onSettingsItemClicked(Settings settings) {
        mPresenter.settingsSelected(settings);
    }
}
