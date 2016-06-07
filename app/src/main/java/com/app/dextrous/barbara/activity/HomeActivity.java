package com.app.dextrous.barbara.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.app.dextrous.barbara.R;
import com.app.dextrous.barbara.callback.CreditTransactionListCallback;
import com.app.dextrous.barbara.callback.InvestmentPlanListCallback;
import com.app.dextrous.barbara.callback.LoginResponseCallback;
import com.app.dextrous.barbara.callback.TransactionListCallback;
import com.app.dextrous.barbara.callback.UserListCallback;
import com.app.dextrous.barbara.callback.UserPreferencesCallback;
import com.app.dextrous.barbara.model.CreditTransaction;
import com.app.dextrous.barbara.model.InvestmentPlan;
import com.app.dextrous.barbara.model.Transaction;
import com.app.dextrous.barbara.model.User;
import com.app.dextrous.barbara.model.UserPreference;
import com.app.dextrous.barbara.response.GenericBeanResponse;
import com.app.dextrous.barbara.response.GenericListResponse;
import com.app.dextrous.barbara.service.BarbaraService;
import com.app.dextrous.barbara.util.AndroidUtil;
import com.app.dextrous.barbara.wrapper.RetrofitWrapper;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

import static com.app.dextrous.barbara.constant.BarbaraConstants.FIELD_PASSWORD;
import static com.app.dextrous.barbara.constant.BarbaraConstants.FIELD_USERNAME;
import static com.app.dextrous.barbara.constant.BarbaraConstants.STRING_BLANK;
import static com.app.dextrous.barbara.constant.BarbaraConstants.TAG;
import static com.app.dextrous.barbara.constant.BarbaraConstants.USER_AUTH_OBJECT_PREFERENCE_KEY;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private HomeActivity self = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.chat);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openChatActivity();
                }
            });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null) {
            drawer.setDrawerListener(toggle);
        }
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }
    }

    private void openChatActivity() {
        Intent intent = new Intent(getApplicationContext(), UserChatActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    public void initLoginView(final int screenToShowNext) {
        final ViewFlipper flipper = (ViewFlipper) findViewById(R.id.navigation_flip_view);
        String BASE_URL = getResources().getString(R.string.base_api_url);
        final BarbaraService apiService = RetrofitWrapper.build(BASE_URL);
        Button loginButton = (Button) findViewById(R.id.loginButton);
        final TextView usernameText = (TextView) findViewById(R.id.usernameText);
        final TextView passwordText = (TextView) findViewById(R.id.passwordText);
        if (loginButton != null
                && usernameText != null
                && passwordText != null) {
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Login and set preference
                    Map<String, Object> form = new HashMap<String, Object>();
                    form.put(FIELD_USERNAME, usernameText.getText().toString());
                    form.put(FIELD_PASSWORD, passwordText.getText().toString());
                    Call<GenericBeanResponse<User>> loginCall = apiService.loginUser(form);
                    Log.d(TAG, form.toString());
                    loginCall.enqueue(new LoginResponseCallback(self, flipper, screenToShowNext));
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        final ViewFlipper flipper = (ViewFlipper) findViewById(R.id.navigation_flip_view);


        String BASE_URL = getResources().getString(R.string.base_api_url);
        final BarbaraService apiService = RetrofitWrapper.build(BASE_URL);

        Integer screenToShowNext = null;
        String userAuthKey = AndroidUtil.getStringPreferenceValue(getApplicationContext(), USER_AUTH_OBJECT_PREFERENCE_KEY);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (flipper != null && drawer != null) {
            if (STRING_BLANK.equals(userAuthKey)) {
                switch (id) {
                    case R.id.nav_transactions:
                        screenToShowNext = 2;
                    case R.id.nav_credit_transactions:
                        if (screenToShowNext == null) {
                            screenToShowNext = 3;
                        }
                    case R.id.nav_chat:
                        if (screenToShowNext == null) {
                            screenToShowNext = 3;
                        }
                    case R.id.nav_preferences:
                        if (screenToShowNext == null) {
                            screenToShowNext = 4;
                        }
                        // Redirect to login page
                        flipper.setDisplayedChild(6);
                        initLoginView(screenToShowNext);
                        // load login screen and quit
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.nav_about:
                    case R.id.nav_users:
                    case R.id.nav_investments:
                        // ignore the non auth screens
                        break;
                }
            }
            if (id == R.id.nav_users) {
                flipper.setDisplayedChild(1);
                // API call
                Call<GenericListResponse<User>> userCall = apiService.getAllUsers();
                userCall.enqueue(new UserListCallback(self, (ListView) findViewById(R.id.userListView)));
            } else if (id == R.id.nav_transactions) {
                flipper.setDisplayedChild(2);
                User userAuth = getLoggedInUser();
                if (userAuth != null) {
                    Call<GenericListResponse<Transaction>> responseCall = apiService.getAllUserTransactions(String.valueOf(userAuth.getId()));
                    responseCall.enqueue(new TransactionListCallback(self, (ListView) findViewById(R.id.transactionListView)));
                }
            } else if (id == R.id.nav_credit_transactions) {
                flipper.setDisplayedChild(3);
                User userAuth = getLoggedInUser();
                if (userAuth != null) {
                    Call<GenericListResponse<CreditTransaction>> responseCall = apiService.getAllUserCreditTransactions(String.valueOf(userAuth.getId()));
                    responseCall.enqueue(new CreditTransactionListCallback(self, (ListView) findViewById(R.id.creditListView)));
                }
            } else if (id == R.id.nav_chat) {
                openChatActivity();
            } else if (id == R.id.nav_preferences) {
                flipper.setDisplayedChild(4);
                TextView nickNameTextView = (TextView) findViewById(R.id.nickNameValueLabel);
                TextView budgetTextView = (TextView) findViewById(R.id.budgetValueLabel);
                TextView securityQuestionTextView = (TextView) findViewById(R.id.securityQuestionValueLabel);
                Button editPreferenceButton = (Button) findViewById(R.id.editPreferencesButton);
                User loggedInUser = getLoggedInUser();
                Call<GenericBeanResponse<UserPreference>> responseCall = apiService.getUserPreference(String.valueOf(loggedInUser.getId()));
                responseCall.enqueue(new UserPreferencesCallback(self, budgetTextView,
                        securityQuestionTextView,
                        nickNameTextView,
                        editPreferenceButton));

            } else if (id == R.id.nav_investments) {
                flipper.setDisplayedChild(5);
                Call<GenericListResponse<InvestmentPlan>> responseCall = apiService.getInvetmentPlans();
                responseCall.enqueue(new InvestmentPlanListCallback(self, (ListView) findViewById(R.id.investmentsListView)));
            } else if (id == R.id.nav_about) {
                flipper.setDisplayedChild(0);
            }
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    private User getLoggedInUser() {
        return AndroidUtil
                .getPreferenceAsObject(getApplicationContext(),
                        USER_AUTH_OBJECT_PREFERENCE_KEY,
                        User.class);
    }
}
