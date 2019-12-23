package models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import ext.GsonExclude;
import helpers.ERConstants;
import play.Logger;
import play.data.validation.Email;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;
import play.libs.Crypto;

@Entity
public class ER_User extends Model {
	
	@Required
    @Email
    @MaxSize(75)
    @Column(unique=true, length=75)
    public String email;
    
    @Required
    @MaxSize(75)
    @Column(length=75)
    public String firstName;
    
    @Required
    @MaxSize(75)
    @Column(length=75)
    public String lastName;
    
    @Required
    @ManyToOne
    public ER_User_Role role;
    
    @MaxSize(30)
    @Column(length=255)
    public String password;
    
    @Required
    public Boolean active;

    @Required
    public Boolean isQAUser;

    @Required
    public Boolean isCommercialQAUser;

    @GsonExclude
    @ManyToOne
    public ER_Channel channel;
    
    @GsonExclude
    @ManyToOne
    public ER_Distributor distributor;
    
    @OneToOne
	public ER_User_Profile profile;
    
    @Column(length=62)
    public String token;

    public Long selectedBroker;

    @GsonExclude
    @OneToOne(mappedBy="user")
    public ER_User_Custom_Logo logo;

    @Required
    public Boolean isCaseAnalyst;

    public Map<String, Object> toMap() {
    	Map<String, Object> map = new HashMap<String,Object>();
    	if (id!=null) {
    		map.put("id", this.id);
    	}
    	if (email!=null) {
    		map.put("email", this.email);
    	}
    	if (firstName!=null) {
    		map.put("firstName", this.firstName);
    	}
    	if (lastName!=null) {
    		map.put("lastName", this.lastName);
    	}
    	if (role!=null) {
    		map.put("role", this.role.toMap());
    	}
    	if (active) {
    		map.put("active", active);
    	}
    	if (profile!=null) {
    		map.put("profile", profile.toMap());
    	}
    	
    	return map;
    }
    
    public String getFullName() {
    	return this.firstName + " " + this.lastName;
    }

    public String getStore() {

        List<ER_Store> store = ER_Store.find("distributor_id = ?", this.distributor).fetch();
        for (ER_Store currentStore: store){
            for (ER_User user : currentStore.sellers){
                if(user.id == this.id) {
                    return currentStore.name;
                }
            }
        }
        return null;
    }
    
    public static ER_User connect(String email, String password) {
        //Encrypt password to search in the database
    	String encryptedPassword = new Crypto().encryptAES(password);
    	Logger.info("este es el password "+encryptedPassword);
    	return find("email = ? and password = ?", email, encryptedPassword).first();
    }

    public boolean showDistributorInQuotation() {

        int roleCode = -1;
        if (role!=null) {
            roleCode = role.code;
        }

        switch (roleCode) {
            case ERConstants.USER_ROLE_SUPER_ADMIN: {
                return false;
            }
            case ERConstants.USER_ROLE_COMMERCIAL_MANAGER: {
                return false;
            }
            case ERConstants.USER_ROLE_CHANNEL_MANAGER: {
                return false;
            }
            case ERConstants.USER_ROLE_SUPERVISOR: {
                List<ER_Store> stores = ER_Store.find("select s from ER_Store s join s.administrators a WHERE a = ? and s.active = true", this).fetch();
                return stores.size() == 1;
            }
            case ERConstants.USER_ROLE_SALES_MAN: {
                return true;
            }
            case ERConstants.USER_ROLE_CABIN_AGENT: {
                return false;
            }
            default: {
                return false;
            }
        }
    }
    
    @PostLoad
    public void decryptPassword() {
    	this.password = new Crypto().decryptAES(this.password);
    }
    
    @PrePersist
    @PreUpdate
    public void encryptPassword() {
    	
    	//Encrypt the password before saving
    	if (password!=null) {
    		this.password = new Crypto().encryptAES(this.password);
    	}
    }

    public void generateToken() {
        UUID uuid = UUID.randomUUID();
        this.token = uuid.toString();
    }

    public Boolean getQAUser() {
        return isQAUser;
    }

    public void setQAUser(Boolean QAUser) {
        isQAUser = QAUser;
    }

    public Boolean getCommercialQAUser() {
        return isCommercialQAUser;
    }

    public void setCommercialQAUser(Boolean QAUser) {
        isCommercialQAUser = QAUser;
    }

    public Boolean getCaseAnalyst() {
        return isCaseAnalyst;
    }

    public void setCaseAnalyst(Boolean caseAnalyst) {
        isCaseAnalyst = caseAnalyst;
    }
}
