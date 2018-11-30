package models;

import play.db.jpa.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
public class ER_Aditional_Multimedia  extends Model {

    @Column(name="url_file")
    public String urlFile;

    @Column(name="is_uploaded")
    public Boolean uploaded;

    @ManyToOne(fetch=FetchType.LAZY)
    public ER_Multimedia multimedia;

    public Boolean isLocalFile(String urlFile){
        return (urlFile != null && !urlFile.contains("http") && !urlFile.contains("https"));
    }

}
