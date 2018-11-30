package models;

import com.google.gson.annotations.Expose;
import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class ER_Admin_Messages  extends Model {
    public Long getId() {
        return id;
    }

    @Expose
    @Column(length=200)
    public String messageName;

    @Column(length=8000)
    public String body;

    @Expose
    @Required
    @Column(name="active")
    public Boolean active;
}
