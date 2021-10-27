package com.example.pasganjil11rpl2.RealmClass;

import android.util.Log;

import com.example.pasganjil11rpl2.Model.HistoryModel;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmHelper {

    private Realm realm;

    public RealmHelper(Realm realm) {
        this.realm = realm;
    }

    //Function untuk menyimpan data
    public void Save(HistoryModel historyModel){
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (realm != null){

                    Number current_id = realm.where(HistoryModel.class).max("id");
                    int nextId;
                    if (current_id == null){
                        nextId = 1;
                    }
                    else {
                        nextId = current_id.intValue() + 1;
                    }

                    historyModel.setId(nextId);
                    HistoryModel model = realm.copyToRealm(historyModel);

                    Log.d("Create", "execute: Database telah dibuat");
                }
                else {
                    Log.d("Create", "execute: Database gagal dibuat");
                }
            }
        });
    }

    //Function untuk memanggil data
    public List<HistoryModel> getAllData(){
        RealmResults<HistoryModel> results = realm.where(HistoryModel.class).findAll();

        return results;
    }

    //Function untuk menghapus data
    public void Delete(int id){
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<HistoryModel> results = realm.where(HistoryModel.class).equalTo("id", id).findAll();
                Log.i("pppppppppppppppp", "getAllData: "+results.size());
                results.deleteFromRealm(0);
            }
        });
    }
}
