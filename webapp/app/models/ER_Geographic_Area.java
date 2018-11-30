package models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import ext.GsonExclude;
import play.Logger;
import play.db.jpa.Model;

@Entity
public class ER_Geographic_Area extends Model{

	@GsonExclude
	@Column(name="name")
	public String name;
	@GsonExclude
	@Column(name="active")
	public Boolean active;
	
	@GsonExclude
	@Column(name="transfer_code")
	public String transferCode;
	
	/** Relations */
	@GsonExclude
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
	@JoinColumn(name="id_father", nullable=true)
	public ER_Geographic_Area father;


    public static Long getObjectId(String transferCode) {
        if (transferCode != null && !transferCode.isEmpty()) {
        	transferCode = transferCode.replace("-", " ");
            ER_Geographic_Area object = find("transferCode = ?", transferCode).first();
            if (object != null) {
                return object.getId();
            }
        }
        return null;
    }
}