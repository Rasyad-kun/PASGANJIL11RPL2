package com.example.pasganjil11rpl2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pasganjil11rpl2.Model.MemoModel;
import com.example.pasganjil11rpl2.Realm.MemoHelper;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class ItemMemoActivity extends AppCompatActivity {
    String title, content;
    Integer id;
    EditText et_title, et_content;
    Realm realm;
    MemoModel memoModel;
    MemoHelper memoHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_memo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Memo");

        try {
            Intent intent = getIntent();
            id = Integer.parseInt(intent.getStringExtra("id"));
            title = intent.getStringExtra("title");
            content = intent.getStringExtra("content");
        } catch (Exception e){
            Log.d("no data found!", e.toString());
        }

        et_title = findViewById(R.id.tv_title_newMemo);
        et_content = findViewById(R.id.tv_content_newMemo);

        if (id != null) {
            et_title.setText(title);
            et_content.setText(content);
        }

        //init
        Realm.init(this);
//        RealmConfiguration configuration = new RealmConfiguration.Builder().allowWritesOnUiThread(true).build();
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .allowWritesOnUiThread(true)
                .build();
        realm = Realm.getInstance(configuration);

        memoHelper = new MemoHelper(realm);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item_memo, menu);
        if (id == null){
            menu.findItem(R.id.nav_edite_itemMemo).setVisible(false);
            menu.findItem(R.id.nav_delete_itemMemo).setVisible(false);
        } else {
            menu.findItem(R.id.nav_save_itemMemo).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.nav_save_itemMemo:
                title = et_title.getText().toString().trim();
                content = et_content.getText().toString().trim();
                memoModel = new MemoModel(title, content);
                memoHelper.save(memoModel);
                Toast.makeText(this, "Saving memo success!", Toast.LENGTH_SHORT).show();
                finish();
                return true;
            case R.id.nav_edite_itemMemo:
                title = et_title.getText().toString().trim();
                content = et_content.getText().toString().trim();
                memoHelper.update(id, title, content);
                Toast.makeText(this, "Edit memo success!", Toast.LENGTH_SHORT).show();
                finish();
                return true;
            case R.id.nav_delete_itemMemo:
                Toast.makeText(this,  title + " has been deleted", Toast.LENGTH_SHORT).show();
                memoHelper.delete(id);
                finish();
//                startActivity(new Intent(HomeActivity.this, FavActivity.class));
//                Toast.makeText(this, "Moving to the favorite list now!", Toast.LENGTH_SHORT).show();
//                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}