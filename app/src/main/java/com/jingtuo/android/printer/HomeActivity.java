package com.jingtuo.android.printer;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jingtuo.android.printer.base.activity.BaseActivity;
import com.jingtuo.android.printer.ui.my.user.add.AddUserActivity;
import com.jingtuo.android.printer.ui.my.user.search.SearchUserActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class HomeActivity extends BaseActivity implements NavController.OnDestinationChangedListener {

    private int id;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected boolean isTitleCentered() {
        return false;
    }

    @Override
    protected boolean isSecondaryPage() {
        return false;
    }

    @Override
    protected void initView() {
        super.initView();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_my_user, R.id.navigation_notifications)
//                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.addOnDestinationChangedListener(this);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
        id = destination.getId();
        setTitle(destination.getLabel());
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        if (id == R.id.navigation_my_user) {
            //我的用户，增加搜索按钮
            MenuItem searchItem = menu.add(id, 0, Menu.NONE, R.string.search_user);
            searchItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            MenuItemCompat.setIconTintList(searchItem, ColorStateList.valueOf(ContextCompat.getColor(this, R.color.tool_bar_icon_tint_color)));
            searchItem.setIcon(R.drawable.ic_search_black_24dp);

            //我的用户，增加添加按钮
            MenuItem addUserItem = menu.add(id, 1, Menu.NONE, R.string.add_user);
            addUserItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            MenuItemCompat.setIconTintList(addUserItem, ColorStateList.valueOf(ContextCompat.getColor(this, R.color.tool_bar_icon_tint_color)));
            addUserItem.setIcon(R.drawable.ic_add_black_24dp);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.navigation_my_user == item.getGroupId()                                                                                                                                                                                                                                                                                                  ) {
            if (0 == item.getItemId()) {
                //搜索用户
                Intent intent = new Intent(this, SearchUserActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                //添加用户
                Intent intent = new Intent(this, AddUserActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
