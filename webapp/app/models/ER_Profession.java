package models;

import javax.persistence.Column;
import javax.persistence.Entity;

import ext.GsonExclude;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class ER_Profession extends Model{

	@GsonExclude
	@MaxSize(75)
	@Column(length=75)
	public String name;
	
	@GsonExclude
	@Required
	public Boolean active;
	
	@GsonExclude
	@Column(name="transfer_code", length=100)
	public String transferCode;

    public static Long getObjectId(String transferCode) {
        if (transferCode != null && !transferCode.isEmpty()) {
            ER_Profession object = find("transferCode = ?", transferCode).first();
            if (object != null) {
                return object.getId();
            }
        }
        return null;
    }
}
