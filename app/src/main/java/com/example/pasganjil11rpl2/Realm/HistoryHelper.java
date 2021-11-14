package com.example.pasganjil11rpl2.Realm;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.pasganjil11rpl2.Model.HistoryModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmModel;
import io.realm.RealmResults;

public class HistoryHelper {
    Realm realm;

    public HistoryHelper(Realm realm){
        this.realm = realm;
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .allowWritesOnUiThread(true)
                .build();
        realm = Realm.getInstance(configuration);
    }

    public static String getCurrentTimes() {
        String currentTime = new SimpleDateFormat("hh:mm", Locale.getDefault()).format(new Date());
        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        return currentTime + ", " + currentDate;
    }

    // untuk menyimpan data
    public void save(final HistoryModel historyModel){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                if (realm != null){
                    Log.e("Created", "Database was created");
                    Number currentIdNum = realm.where(HistoryModel.class).max("id");
                    int nextId;
                    if (currentIdNum == null){
                        nextId = 1;
                    }else {
                        nextId = currentIdNum.intValue() + 1;
                    }
                    historyModel.setId(nextId);
                    HistoryModel model = realm.copyToRealm(historyModel);
                }else{
                    Log.e("ppppp", "execute: Database not Exist");
                }
            }
        });
    }

    // untuk memanggil semua data
    public List<HistoryModel> getAllHistory(){
        RealmResults<HistoryModel> results = realm.where(HistoryModel.class).findAll();
        return results;
    }

    // untuk menghapus data
    public void delete(Integer id){
        final RealmResults<HistoryModel> model = realm.where(HistoryModel.class).equalTo("id", id).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                model.deleteFromRealm(0);
            }
        });
    }

    // untuk menghapus seluruh data
    public void deleteAll(){
        final RealmResults<HistoryModel> model = realm.where(HistoryModel.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                realm.delete(HistoryModel.class);
            }
        });
    }
}
