package cz.vasic2000.icash.mvp.model.entity.realm;


import androidx.room.PrimaryKey;

import io.realm.RealmObject;

public class RealmRepository extends RealmObject {
    @PrimaryKey
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}