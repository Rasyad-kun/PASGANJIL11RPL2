package com.example.pasganjil11rpl2.Realm;

import android.util.Log;

import com.example.pasganjil11rpl2.Model.HistoryModel;
import com.example.pasganjil11rpl2.Model.MemoModel;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class MemoHelper {
    Realm realm;

    public MemoHelper(Realm realm){
        this.realm = realm;
    }

    // untuk menyimpan data
    public void save(final MemoModel memoModel){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (realm != null){
                    Log.e("Created", "Database was created");
                    Number currentIdNum = realm.where(MemoModel.class).max("id");
                    int nextId;
                    if (currentIdNum == null){
                        nextId = 1;
                    }else {
                        nextId = currentIdNum.intValue() + 1;
                    }
                    memoModel.setId(nextId);
                    MemoModel model = realm.copyToRealm(memoModel);
                }else{
                    Log.e("ppppp", "execute: Database not Exist");
                }
            }
        });
    }

    // untuk memanggil semua data
    public List<MemoModel> getAllHistory(){
        RealmResults<MemoModel> results = realm.where(MemoModel.class).findAll();
        return results;
    }

    // untuk meng-update data
    public void update(final Integer id, final String title, final String content){
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                MemoModel model = realm.where(MemoModel.class)
                        .equalTo("id", id)
                        .findFirst();
                model.setTitle(title);
                model.setContent(content);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.e("pppp", "onSuccess: Update Successfully");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });
    }

    // untuk menghapus data
    public void delete(Integer id){
        final RealmResults<MemoModel> model = realm.where(MemoModel.class).equalTo("id", id).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                model.deleteFromRealm(0);
            }
        });
    }

}
