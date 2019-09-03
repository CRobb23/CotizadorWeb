package models;

import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.util.HashMap;
import java.util.Map;

@Entity
public class ER_Distributor_Custom_Logo extends Model {

    @Required
    @Column(length=75)
    public String logoName;

    @Required
    @Column(length=75)
    public String bannerName;

    @Required
    public Boolean active;

    @Required
    @OneToOne
    public ER_Distributor distributor;

    public Map<String, Object> toMap(){
        Map<String, Object> map = new HashMap<String, Object>();

        if(this.id!=null){
            map.put("id", this.id);
        }
        if(this.logoName!=null){
            map.put("logoName", this.logoName);
        }
        if(this.bannerName!=null){
            map.put("bannerName", this.bannerName);
        }
        if(this.active!=null){
            map.put("active", this.active);
        }
        if(this.distributor!=null){
            map.put("distributor", this.distributor.toMap());
        }

        return map;
    }

    public int activeIntValue(){
        return active != null && active.booleanValue() ? 1 : 0;
    }

}
