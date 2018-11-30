package models;

import javax.persistence.*;

import play.db.jpa.Model;
import utils.StringUtil;

@Entity
public class ER_Client_PEP extends Model {

    @Column(name = "Relationship_Is_pep")
    public Boolean relationshipIsPep;

    @Column(name = "Type_of_relationship", length = 75)
    public String typeOfrelationship;

    @Column(name = "Specific_relationship", length = 75)
    public String specificRelationship;

    @Column(name = "Relationship", length = 75)
    public String relationship;

    @Column(name = "Relation_is_national")
    public Boolean relationIsNational;

    @Column(name = "Relation_fist_name", length = 75)
    public String relationFirtName;
    @Column(name = "Relation_Second_name", length = 75)
    public String relationSecondName;
    @Column(name = "Relation_First_surname", length = 75)
    public String relationFirstSurname;
    @Column(name = "Relation_Second_surname", length = 75)
    public String relationSecondSurname;
    @Column(name = "Married_surname", length = 50)
    public String relationMarriedSurname;
    @Column(name = "Relation_other_name", length = 75)
    public String relationOtherName;

    @Column(name = "Relation_sex", length = 50)
    public String relationSex;
    @Column(name = "Relation_company_name", length = 75)
    public String relationCompanyName;

    @Column(name = "Relation_Job", length = 75)
    public String relationJob;

    /**Relations*/
    @ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
    @JoinColumn(name="Id_company_country", nullable=true)
    public ER_Geographic_Area companyCountry;

    @ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
    @JoinColumn(name="Id_Relation_company_country", nullable=true)
    public ER_Geographic_Area idRelationCompanyCountry;

    public void ConvertUpper(){

        if (!StringUtil.isNullOrBlank(this.typeOfrelationship)) {
            this.typeOfrelationship =this.typeOfrelationship.toUpperCase();
        }
        if (!StringUtil.isNullOrBlank(this.specificRelationship)) {
            this.specificRelationship= this.specificRelationship.toUpperCase();
        }
        if (!StringUtil.isNullOrBlank(this.relationship)) {
            this.relationship= this.relationship.toUpperCase();
        }
        if (!StringUtil.isNullOrBlank(this.relationFirtName)) {
            this.relationFirtName= this.relationFirtName.toUpperCase();
        }
        if (!StringUtil.isNullOrBlank(this.relationSecondName)) {
            this.relationSecondName= this.relationSecondName.toUpperCase();
        }
        if (!StringUtil.isNullOrBlank(this.relationFirstSurname)) {
            this.relationFirstSurname= this.relationFirstSurname.toUpperCase();
        }
        if (!StringUtil.isNullOrBlank(this.relationSecondSurname)) {
            this.relationSecondSurname = this.relationSecondSurname.toUpperCase();
        }
        if (!StringUtil.isNullOrBlank(this.relationMarriedSurname)) {
            this.relationMarriedSurname =this.relationMarriedSurname.toUpperCase();
        }
        if (!StringUtil.isNullOrBlank(this.relationOtherName)) {
            this.relationOtherName= this.relationOtherName.toUpperCase();
        }
        if (!StringUtil.isNullOrBlank(this.relationCompanyName)) {
            this.relationCompanyName =this.relationCompanyName.toUpperCase();
        }
        if (!StringUtil.isNullOrBlank(this.relationJob)) {
            this.relationJob =this.relationJob.toUpperCase();
        }
    }



}

