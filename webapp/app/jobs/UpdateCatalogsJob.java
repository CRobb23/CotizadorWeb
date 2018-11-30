package jobs;

import models.*;
import play.Play;
import play.jobs.Every;
import play.jobs.Job;

import java.io.*;


@Every("23h")
public class UpdateCatalogsJob extends Job {

    private static String SERVER_URL = Play.configuration.getProperty("catalogServer");

    private static String[] filesArray = {"ctazonO.txt", "ctazoio.txt", "ctatviO.txt", "ctattaO.txt", "ctatplO.txt",
            "ctatliO.txt", "ctatipO.txt", "ctatdoO.txt", "ctatcdO.txt", "ctatcbO.txt", "ctatcaO.txt", "ctapfvhO.txt",
            "ctatalO.txt", "ctasmwO.txt", "ctasexO.txt", "ctapvnO.txt", "ctapsoO.txt", "ctaproO.txt", "ctanacO.txt",
            "ctamunO.txt", "ctamnaO.txt", "ctamliO.txt", "ctamcoO.txt", "ctalinO.txt", "ctafpaO.txt", "ctaercO.txt",
            "ctadepO.txt", "ctadarO.txt", "ctacobO.txt", "ctaccdO.txt", "ctaageO.txt", "ctaaecO.txt", "ctaeciO.txt",
            "ctamwoO.txt"};




    public void doJob() {
        //Read each file
        for (String file : filesArray)
            try {
                readFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    void readFile(String archivo) throws FileNotFoundException, IOException {
        String cadena;
        FileReader f = null;
        File file = new File(SERVER_URL + archivo);
        try {
            switch (archivo) {
                //GeographicAREA
                //Pendiente
                /*
                case "ctazonO.txt":
                    if (file.exists()) {
                        f = new FileReader(file);
                        BufferedReader b = new BufferedReader(f);
                        ER_Geographic_Area place;
                        while ((cadena = b.readLine()) != null) {
                            place = ER_Geographic_Area.find("transfer_code = ?", cadena.split("\\|")[0]).first();
                            if(place == null){
                                place = new ER_Geographic_Area();
                                place.transferCode = cadena.split("\\|")[0];
                                place.name = cadena.split("\\|")[1];
                                place.save();
                            }
                            else {
                                if (!place.name.equals(cadena.split("\\|")[1])) {
                                    place.name = cadena.split("\\|")[1];
                                    place.save();
                                }
                            }
                        }
                        b.close();
                    }
                    break;
                    */
                    //ER_ZONE
                case "ctazoio.txt":
                    if (file.exists()) {
                        f = new FileReader(file);
                        BufferedReader b = new BufferedReader(f);
                        ER_Zone place;
                        while ((cadena = b.readLine()) != null) {
                            place = ER_Zone.find("transfer_code = ?", cadena.split("\\|")[0]).first();
                            if(place == null){
                                place = new ER_Zone();
                                place.transferCode = cadena.split("\\|")[0];
                                place.name = cadena.split("\\|")[1];
                                place.active = true;
                                place.save();
                            }
                            else {
                                if (!place.name.equals(cadena.split("\\|")[1])) {
                                    place.name = cadena.split("\\|")[1];
                                    place.save();
                                }
                            }
                        }
                        b.close();
                    }
                    break;
                    //vehicle_type
                case "ctatviO.txt":
                    if (file.exists()) {
                        f = new FileReader(file);
                        BufferedReader b = new BufferedReader(f);
                        ER_Vehicle_Type type;
                        while ((cadena = b.readLine()) != null) {
                            type = ER_Vehicle_Type.find("transfer_code = ?", cadena.split("\\|")[0]).first();
                            if(type == null){
                                type = new ER_Vehicle_Type();
                                type.transferCode = cadena.split("\\|")[0];
                                type.name = cadena.split("\\|")[1];
                                type.active =true;
                                type.save();
                            }
                            else {
                                if (!type.name.equals(cadena.split("\\|")[1])) {
                                    type.name = cadena.split("\\|")[1];
                                    type.save();
                                }
                            }
                        }
                        b.close();
                    }
                    break;
                //Pendiente, no se cual tabla es./*
                /*
                case "ctattaO.txt":
                    file = new File(SERVER_URL + archivo);
                    if (file.exists()) {
                        f = new FileReader(file);
                        BufferedReader b = new BufferedReader(f);
                        ER_Vehicle_Type type;
                        while ((cadena = b.readLine()) != null) {
                            type = ER_Vehicle_Type.find("transfer_code = ?", cadena.split("\\|")[0]).first();
                            if(type == null){
                                type = new ER_Vehicle_Type();
                                type.transferCode = cadena.split("\\|")[0];
                                type.name = cadena.split("\\|")[1];
                                type.save();
                            }
                            else {
                                 if (!type.name.equals(cadena.split("\\|")[1])) {
                                    type.name = cadena.split("\\|")[1];
                                    type.save();
                                }
                            }
                        }
                        b.close();
                    }
                    break;*/
                //Plate Type
                case "ctatplO.txt":
                    if (file.exists()) {
                        f = new FileReader(file);
                        BufferedReader b = new BufferedReader(f);
                        ER_Plate_Type type;
                        while ((cadena = b.readLine()) != null) {
                            type = ER_Plate_Type.find("transfer_code = ?", cadena.split("\\|")[0]).first();
                            if(type == null){
                                type = new ER_Plate_Type();
                                type.transferCode = cadena.split("\\|")[0];
                                type.name = cadena.split("\\|")[1];
                                type.active = true;
                                type.save();
                            }
                            else {
                                if (!type.name.equals(cadena.split("\\|")[1])) {
                                    type.name = cadena.split("\\|")[1];
                                    type.save();
                                }
                            }
                        }
                        b.close();
                    }
                    break;
                //license Type
                case "ctatliO.txt":
                    if (file.exists()) {
                        f = new FileReader(file);
                        BufferedReader b = new BufferedReader(f);
                        ER_License_Type type;
                        while ((cadena = b.readLine()) != null) {
                            type = ER_License_Type.find("transfer_code = ?", cadena.split("\\|")[0]).first();
                            if(type == null){
                                type = new ER_License_Type();
                                type.transferCode = cadena.split("\\|")[0];
                                type.name = cadena.split("\\|")[1];
                                type.active = true;
                                type.save();
                            }
                            else {
                                if (!type.name.equals(cadena.split("\\|")[1])) {
                                    type.name = cadena.split("\\|")[1];
                                    type.save();
                                }
                            }
                        }
                        b.close();
                    }
                    break;
                //No se cual tabla es
                /*
                case "ctatipO.txt":
                    file = new File(SERVER_URL + archivo);
                    if (file.exists()) {
                        f = new FileReader(file);
                        BufferedReader b = new BufferedReader(f);
                        ER_License_Type type;
                        while ((cadena = b.readLine()) != null) {
                            type = ER_License_Type.find("transfer_code = ?", cadena.split("\\|")[0]).first();
                            if(type == null){
                                type = new ER_license_type();
                                type.transferCode = cadena.split("\\|")[0];
                                type.name = cadena.split("\\|")[1];
                                type.save();
                            }
                            else {
                                if (!type.name.equals(cadena.split("\\|")[1])) {
                                    type.name = cadena.split("\\|")[1];
                                    type.save();
                                }
                            }
                        }
                        b.close();
                    }
                    break;*/
                //No se cual tabla es
                /*
                case "ctatdoO.txt":
                    file = new File(SERVER_URL + archivo);
                    if (file.exists()) {
                        f = new FileReader(file);
                        BufferedReader b = new BufferedReader(f);
                        ER_License_Type type;
                        while ((cadena = b.readLine()) != null) {
                            type = ER_License_Type.find("transfer_code = ?", cadena.split("\\|")[0]).first();
                            if(type == null){
                                type.transferCode = cadena.split("\\|")[0];
                                type.name = cadena.split("\\|")[1];
                                type.save();
                            }
                            else {
                                if (!type.name.equals(cadena.split("\\|")[1])) {
                                    type.name = cadena.split("\\|")[1];
                                    type.save();
                                }
                            }
                        }
                        b.close();
                    }
                    break;*/
                //Tarjetas de credito
                case "ctatcdO.txt":
                    if (file.exists()) {
                        f = new FileReader(file);
                        BufferedReader b = new BufferedReader(f);
                        ER_Card_Class type;
                        while ((cadena = b.readLine()) != null) {
                            type = ER_Card_Class.find("transfer_code2 = ?", cadena.split("\\|")[0]).first();
                            if(type == null){
                                type = new ER_Card_Class();
                                type.transferCode2 = cadena.split("\\|")[0];
                                type.name = cadena.split("\\|")[1];
                                type.active = true;
                                type.save();
                            }
                            else {
                                if (!type.name.equals(cadena.split("\\|")[1])) {
                                    type.name = cadena.split("\\|")[1];
                                    type.save();
                                }
                            }
                        }
                        b.close();
                    }
                    break;
                //Bancos
                case "ctatcbO.txt":
                    if (file.exists()) {
                        f = new FileReader(file);
                        BufferedReader b = new BufferedReader(f);
                        ER_Bank type;
                        while ((cadena = b.readLine()) != null) {
                            type = ER_Bank.find("transfer_code = ?", cadena.split("\\|")[0]).first();
                            if(type == null){
                                type = new ER_Bank();
                                type.transferCode = cadena.split("\\|")[0];
                                type.name = cadena.split("\\|")[1];
                                type.active = true;
                                type.save();
                            }
                            else {
                                if (!type.name.equals(cadena.split("\\|")[1])) {
                                    type.name = cadena.split("\\|")[1];
                                    type.save();
                                }
                            }
                        }
                        b.close();
                    }
                    break;
                //No se que tabla es
                /*
                case "ctatcaO.txt":
                    if (file.exists()) {
                        f = new FileReader(file);
                        BufferedReader b = new BufferedReader(f);
                        ER_Bank type;
                        while ((cadena = b.readLine()) != null) {
                            type = ER_Bank.find("transfer_code = ?", cadena.split("\\|")[0]).first();
                            if(type == null){
                                type.transferCode = cadena.split("\\|")[0];
                                type.name = cadena.split("\\|")[1];
                                type.save();
                            }
                            else {
                                if (!type.name.equals(cadena.split("\\|")[1])) {
                                    type.name = cadena.split("\\|")[1];
                                    type.save();
                                }
                            }
                        }
                        b.close();
                    }
                    break;*/
                //Reminder_type
                case "ctatalO.txt":
                    if (file.exists()) {
                        f = new FileReader(file);
                        BufferedReader b = new BufferedReader(f);
                        ER_Reminder_Type type;
                        while ((cadena = b.readLine()) != null) {
                            type = ER_Reminder_Type.find("transfer_code = ?", cadena.split("\\|")[0]).first();
                            if(type == null){
                                type = new ER_Reminder_Type();
                                type.transferCode = cadena.split("\\|")[0];
                                type.name = cadena.split("\\|")[1];
                                type.active = true;
                                type.save();
                            }
                            else {
                                if (!type.name.equals(cadena.split("\\|")[1])) {
                                    type.name = cadena.split("\\|")[1];
                                    type.save();
                                }
                            }
                        }
                        b.close();
                    }
                    break;
                //No se que tabla es
                /*
                case "ctasmwO.txt":
                    file = new File(SERVER_URL + archivo);
                    if (file.exists()) {
                        f = new FileReader(file);
                        BufferedReader b = new BufferedReader(f);
                        ER_Reminder_Type type;
                        while ((cadena = b.readLine()) != null) {
                            type = ER_Reminder_Type.find("transfer_code = ?", cadena.split("\\|")[0]).first();
                            if(type == null){
                                type.transferCode = cadena.split("\\|")[0];
                                type.name = cadena.split("\\|")[1];
                                type.save();
                            }
                            else {
                                if (!type.name.equals(cadena.split("\\|")[1])) {
                                    type.name = cadena.split("\\|")[1];
                                    type.save();
                                }
                            }
                        }
                        b.close();
                    }
                    break;*/
                //Sexo
                case "ctasexO.txt":
                    if (file.exists()) {
                        f = new FileReader(file);
                        BufferedReader b = new BufferedReader(f);
                        ER_Sex type;
                        while ((cadena = b.readLine()) != null) {
                            type = ER_Sex.find("transfer_code = ?", cadena.split("\\|")[0]).first();
                            if(type == null){
                                type = new ER_Sex();
                                type.transferCode = cadena.split("\\|")[0];
                                type.name = cadena.split("\\|")[1];
                                type.active = true;
                                type.save();
                            }
                            else {
                                if (!type.name.equals(cadena.split("\\|")[1])) {
                                    type.name = cadena.split("\\|")[1];
                                    type.save();
                                }
                            }
                        }
                        b.close();
                    }
                    break;
                //No se que tabla es
                /*
                case "ctapvnO.txt":
                    file = new File(SERVER_URL + archivo);
                    if (file.exists()) {
                        f = new FileReader(file);
                        BufferedReader b = new BufferedReader(f);
                        ER_Sex type;
                        while ((cadena = b.readLine()) != null) {
                            type = ER_Sex.find("transfer_code = ?", cadena.split("\\|")[0]).first();
                            if(type == null){
                                type.transferCode = cadena.split("\\|")[0];
                                type.name = cadena.split("\\|")[1];
                                type.save();
                            }
                            else {
                                if (!type.name.equals(cadena.split("\\|")[1])) {
                                    type.name = cadena.split("\\|")[1];
                                    type.save();
                                }
                            }
                        }
                        b.close();
                    }
                    break;*/
                    //Typo de sociedad
                case "ctapsoO.txt":
                    if (file.exists()) {
                        f = new FileReader(file);
                        BufferedReader b = new BufferedReader(f);
                        ER_Society_Type type;
                        while ((cadena = b.readLine()) != null) {
                            type = ER_Society_Type.find("transfer_code = ?", cadena.split("\\|")[0]).first();
                            if(type == null){
                                type = new ER_Society_Type();
                                type.transferCode = cadena.split("\\|")[0];
                                type.name = cadena.split("\\|")[1];
                                type.active = true;
                                type.save();
                            }
                            else {
                                if (!type.name.equals(cadena.split("\\|")[1])) {
                                    type.name = cadena.split("\\|")[1];
                                    type.save();
                                }
                            }
                        }
                        b.close();
                    }
                    break;
                    //Profesion
                case "ctaproO.txt":
                    if (file.exists()) {
                        f = new FileReader(file);
                        BufferedReader b = new BufferedReader(f);
                        ER_Profession type;
                        while ((cadena = b.readLine()) != null) {
                            type = ER_Profession.find("transfer_code = ?", cadena.split("\\|")[0]).first();
                            if(type == null){
                                type = new ER_Profession();
                                type.transferCode = cadena.split("\\|")[0];
                                type.name = cadena.split("\\|")[1];
                                type.active = true;
                                type.save();
                            }
                            else {
                                if (!type.name.equals(cadena.split("\\|")[1])) {
                                    type.name = cadena.split("\\|")[1];
                                    type.save();
                                }
                            }
                        }
                        b.close();
                    }
                    break;
                //No se que tabla es
                /*
                case "ctapfvhO.txt":
                    file = new File(SERVER_URL + archivo);
                    if (file.exists()) {
                        f = new FileReader(file);
                        BufferedReader b = new BufferedReader(f);
                        ER_Profession type;
                        while ((cadena = b.readLine()) != null) {
                            type = ER_Profession.find("transfer_code = ?", cadena.split("\\|")[0]).first();
                            if(type == null){
                                type.transferCode = cadena.split("\\|")[0];
                                type.name = cadena.split("\\|")[1];
                                type.save();
                            }
                            else {
                                if (!type.name.equals(cadena.split("\\|")[1])) {
                                    type.name = cadena.split("\\|")[1];
                                    type.save();
                                }
                            }
                        }
                        b.close();
                    }
                    break;*/
                //Nacionalidad
                case "ctanacO.txt":
                    if (file.exists()) {
                        f = new FileReader(file);
                        BufferedReader b = new BufferedReader(f);
                        ER_Nationality type;
                        while ((cadena = b.readLine()) != null) {
                            type = ER_Nationality.find("transfer_code = ?", cadena.split("\\|")[0]).first();
                            if(type == null){
                                type = new ER_Nationality();
                                type.transferCode = cadena.split("\\|")[0];
                                type.name = cadena.split("\\|")[1];
                                type.active = true;
                                type.save();
                            }
                            else {
                                if (!type.name.equals(cadena.split("\\|")[1])) {
                                    type.name = cadena.split("\\|")[1];
                                    type.save();
                                }
                            }
                        }
                        b.close();
                    }
                    break;
                //Municipios Pendiente
                /*
                case "ctamunO.txt":
                    file = new File(SERVER_URL + archivo);
                    if (file.exists()) {
                        f = new FileReader(file);
                        BufferedReader b = new BufferedReader(f);
                        ER_Nationality type;
                        while ((cadena = b.readLine()) != null) {
                            type = ER_Nationality.find("transfer_code = ?", cadena.split("\\|")[0]).first();
                            if(type == null){
                                type.transferCode = cadena.split("\\|")[0];
                                type.name = cadena.split("\\|")[1];
                                type.save();
                            }
                            else {
                               if (!type.name.equals(cadena.split("\\|")[1])) {
                                    type.name = cadena.split("\\|")[1];
                                    type.save();
                                }
                            }
                        }
                        b.close();
                    }
                    break;*/
                //Pendiente
                /*
                case "ctamnaO.txt":
                    if (file.exists()) {
                        f = new FileReader(file);
                        BufferedReader b = new BufferedReader(f);
                        ER_Nationality type;
                        while ((cadena = b.readLine()) != null) {
                            type = ER_Nationality.find("transfer_code = ?", cadena.split("\\|")[0]).first();
                            if(type == null){
                                type.transferCode = cadena.split("\\|")[0];
                                type.name = cadena.split("\\|")[1];
                                type.save();
                            }
                            else {
                                if (!type.name.equals(cadena.split("\\|")[1])) {
                                    type.name = cadena.split("\\|")[1];
                                    type.save();
                                }
                            }
                        }
                        b.close();
                    }
                    break;*/
                //Linea, Pendiente
                /*
                case "ctamliO.txt":
                    file = new File(SERVER_URL + archivo);
                    if (file.exists()) {
                        f = new FileReader(file);
                        BufferedReader b = new BufferedReader(f);
                        ER_Nationality type;
                        while ((cadena = b.readLine()) != null) {
                            type = ER_Nationality.find("transfer_code = ?", cadena.split("\\|")[0]).first();
                            if(type == null){
                                type.transferCode = cadena.split("\\|")[0];
                                type.name = cadena.split("\\|")[1];
                                type.save();
                            }
                            else {
                                if (!type.name.equals(cadena.split("\\|")[1])) {
                                    type.name = cadena.split("\\|")[1];
                                    type.save();
                                }
                            }
                        }
                        b.close();
                    }
                    break;*/
                //Formas de pago, Charge Type

                case "ctamcoO.txt":
                    if (file.exists()) {
                        f = new FileReader(file);
                        BufferedReader b = new BufferedReader(f);
                        ER_Charge_Type type;
                        while ((cadena = b.readLine()) != null) {
                            type = ER_Charge_Type.find("transfer_code = ?", cadena.split("\\|")[0]).first();
                            if(type == null){
                                type = new ER_Charge_Type();
                                type.transferCode = cadena.split("\\|")[0];
                                type.name = cadena.split("\\|")[1];
                                type.active = true;
                                type.save();
                            }
                            else {
                                if (!type.name.equals(cadena.split("\\|")[1])) {
                                    type.name = cadena.split("\\|")[1];
                                    type.save();
                                }
                            }
                        }
                        b.close();
                    }
                    break;

                //Channel
                case "ctalinO.txt":
                    if (file.exists()) {
                        f = new FileReader(file);
                        BufferedReader b = new BufferedReader(f);
                        ER_Channel type;
                        while ((cadena = b.readLine()) != null) {
                            type = ER_Channel.find("transfer_code = ?", cadena.split("\\|")[0]).first();
                            if(type == null){
                                type = new ER_Channel();
                                type.transferCode = cadena.split("\\|")[0];
                                type.name = cadena.split("\\|")[1];
                                type.active = true;
                                type.save();
                            }
                            else {
                                if (!type.name.equals(cadena.split("\\|")[1])) {
                                    type.name = cadena.split("\\|")[1];
                                    type.save();
                                }
                            }
                        }
                        b.close();
                    }
                    break;
                //Periodicidad de pago payment Type
                /*
                case "ctafpaO.txt":
                    if (file.exists()) {
                        f = new FileReader(file);
                        BufferedReader b = new BufferedReader(f);
                        ER_Payment_Type type;
                        while ((cadena = b.readLine()) != null) {
                            type = ER_Channel.find("transfer_code = ?", cadena.split("\\|")[0]).first();
                            if(type == null){
                                type = new ER_Payment_Type();
                                type.transferCode = cadena.split("\\|")[0];
                                type.name = cadena.split("\\|")[1];
                                type.active = true;
                                type.save();
                            }
                            else {
                                if (!type.name.equals(cadena.split("\\|")[1])) {
                                    type.name = cadena.split("\\|")[1];
                                    type.save();
                                }
                            }
                        }
                        b.close();
                    }
                    break;
                //No encuentro la tabla
                /*
                case "ctaercO.txt":
                    if (file.exists()) {
                        f = new FileReader(file);
                        BufferedReader b = new BufferedReader(f);
                        ER_Channel type;
                        while ((cadena = b.readLine()) != null) {
                            type = ER_Channel.find("transfer_code = ?", cadena.split("\\|")[0]).first();
                            if(type == null){
                                type.transferCode = cadena.split("\\|")[0];
                                type.name = cadena.split("\\|")[1];
                                type.active = true;
                                type.save();
                            }
                            else {
                                if (!type.name.equals(cadena.split("\\|")[1])) {
                                    type.name = cadena.split("\\|")[1];
                                    type.save();
                                }
                            }
                        }
                        b.close();
                    }
                    break;
                    */
                //Civil Status
                case "ctaeciO.txt":
                    if (file.exists()) {
                        f = new FileReader(file);
                        BufferedReader b = new BufferedReader(f);
                        ER_Civil_Status type;
                        while ((cadena = b.readLine()) != null) {
                            type = ER_Civil_Status.find("transfer_code = ?", cadena.split("\\|")[0]).first();
                            if(type == null){
                                type = new ER_Civil_Status();
                                type.transferCode = cadena.split("\\|")[0];
                                type.name = cadena.split("\\|")[1];
                                type.active = true;
                                type.save();
                            }
                            else {
                                if (!type.name.equals(cadena.split("\\|")[1])) {
                                    type.name = cadena.split("\\|")[1];
                                    type.save();
                                }
                            }
                        }
                        b.close();
                    }
                    break;
                //Departamento, Pendiente
                /*
                case "ctadepO.txt":
                    file = new File(SERVER_URL + archivo);
                    if (file.exists()) {
                        f = new FileReader(file);
                        BufferedReader b = new BufferedReader(f);
                        ER_Civil_Status type;
                        while ((cadena = b.readLine()) != null) {
                            type = ER_Civil_Status.find("transfer_code = ?", cadena.split("\\|")[0]).first();
                            if(type == null){
                                type.transferCode = cadena.split("\\|")[0];
                                type.name = cadena.split("\\|")[1];
                                type.save();
                            }
                            else {
                                if (!type.name.equals(cadena.split("\\|")[1])) {
                                    type.name = cadena.split("\\|")[1];
                                    type.save();
                                }
                            }
                        }
                        b.close();
                    }
                    break;
*/
                //No encuentro tabla
                /*
                case "ctadarO.txt":
                    file = new File(SERVER_URL + archivo);
                    if (file.exists()) {
                        f = new FileReader(file);
                        BufferedReader b = new BufferedReader(f);
                        ER_Civil_Status type;
                        while ((cadena = b.readLine()) != null) {
                            type = ER_Civil_Status.find("transfer_code = ?", cadena.split("\\|")[0]).first();
                            if(type == null){
                                type.transferCode = cadena.split("\\|")[0];
                                type.name = cadena.split("\\|")[1];
                                type.save();
                            }
                            else {
                                if (!type.name.equals(cadena.split("\\|")[1])) {
                                    type.name = cadena.split("\\|")[1];
                                    type.save();
                                }
                            }
                        }
                        b.close();
                    }
                    break;
                    */
                //Pendiente Coberturas
                /*
                case "ctacobO.txt":
                    file = new File(SERVER_URL + archivo);
                    if (file.exists()) {
                        f = new FileReader(file);
                        BufferedReader b = new BufferedReader(f);
                        ER_Civil_Status type;
                        while ((cadena = b.readLine()) != null) {
                            type = ER_Civil_Status.find("transfer_code = ?", cadena.split("\\|")[0]).first();
                            if(type == null){
                                type.transferCode = cadena.split("\\|")[0];
                                type.name = cadena.split("\\|")[1];
                                type.save();
                            }
                            else {
                                if (!type.name.equals(cadena.split("\\|")[1])) {
                                    type.name = cadena.split("\\|")[1];
                                    type.save();
                                }
                            }
                        }
                        b.close();
                    }
                    break;
                    */
                //No encuentro tabla
                /*
                case "ctaccdO.txt":
                    file = new File(SERVER_URL + archivo);
                    if (file.exists()) {
                        f = new FileReader(file);
                        BufferedReader b = new BufferedReader(f);
                        ER_Civil_Status type;
                        while ((cadena = b.readLine()) != null) {
                            type = ER_Civil_Status.find("transfer_code = ?", cadena.split("\\|")[0]).first();
                            if(type == null){
                                type.transferCode = cadena.split("\\|")[0];
                                type.name = cadena.split("\\|")[1];
                                type.save();
                            }
                            else {
                                if (!type.name.equals(cadena.split("\\|")[1])) {
                                    type.name = cadena.split("\\|")[1];
                                    type.save();
                                }
                            }
                        }
                        b.close();
                    }
                    break;
                    */
                //No encuentro tabla
                /*
                case "ctaageO.txt":
                    file = new File(SERVER_URL + archivo);
                    if (file.exists()) {
                        f = new FileReader(file);
                        BufferedReader b = new BufferedReader(f);
                        ER_Civil_Status type;
                        while ((cadena = b.readLine()) != null) {
                            type = ER_Civil_Status.find("transfer_code = ?", cadena.split("\\|")[0]).first();
                            if(type == null){
                                type.transferCode = cadena.split("\\|")[0];
                                type.name = cadena.split("\\|")[1];
                                type.save();
                            }
                            else {
                                if (!type.name.equals(cadena.split("\\|")[1])) {
                                    type.name = cadena.split("\\|")[1];
                                    type.save();
                                }
                            }
                        }
                        b.close();
                    }
                    break;
                    */
                //Actividad economica
                case "ctaaecO.txt":
                    if (file.exists()) {
                        f = new FileReader(file);
                        BufferedReader b = new BufferedReader(f);
                        ER_Economic_Activity type;
                        while ((cadena = b.readLine()) != null) {
                            type = ER_Economic_Activity.find("transfer_code = ?", cadena.split("\\|")[0]).first();
                            if(type == null){
                                type = new ER_Economic_Activity();
                                type.transferCode = cadena.split("\\|")[0];
                                type.name = cadena.split("\\|")[1];
                                type.active = true;
                                type.save();
                            }
                            else {
                                if (!type.name.equals(cadena.split("\\|")[1])) {
                                    type.name = cadena.split("\\|")[1];
                                    type.save();
                                }
                            }
                        }
                        b.close();
                    }
                    break;
                //No encuentro tabla
                /*
                case "ctamwoO.txt":
                    if (file.exists()) {
                        f = new FileReader(file);
                        BufferedReader b = new BufferedReader(f);
                        ER_Economic_Activity type;
                        while ((cadena = b.readLine()) != null) {
                            type = ER_Economic_Activity.find("transfer_code = ?", cadena.split("\\|")[0]).first();
                            if(type == null){
                                type.transferCode = cadena.split("\\|")[0];
                                type.name = cadena.split("\\|")[1];
                                type.active = true;
                                type.save();
                            }
                            else {
                                if (!type.name.equals(cadena.split("\\|")[1])) {
                                    type.name = cadena.split("\\|")[1];
                                    type.save();
                                }
                            }
                        }
                        b.close();
                    }
                    break;*/
                default:
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            // En el finally cerramos el fichero, para asegurarnos
            // que se cierra tanto si to do va bien como si salta
            // una excepcion.
            try{
                if( null != f){
                    f.close();
                }
            }catch (Exception e2){
                e2.printStackTrace();
            }
        }
    }
}
