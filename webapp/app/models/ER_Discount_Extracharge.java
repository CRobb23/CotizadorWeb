package models;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by GEKO on 11/11/19.
 */
@Entity
public class ER_Discount_Extracharge extends Model {
    @Required
    @MaxSize(75)
    @Column(length=75)
    public String name;

    @Required
    @Column(name="active")
    public Boolean active;

    @Required
    public Double value;
}
