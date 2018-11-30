package models.dto;

import models.*;
import models.ws.QueryPersonDetailResponse;

public class PersonDetailDTO {

    private String taxNumber;
    private String firstName;
    private String secondName;
    private String firstSurname;
    private String secondSurname;
    private String marriedSurname;
    private String birthdate;
    private Long sex;
    private Integer age;
    private Long profession;
    private String identificationDocument;
    private String codeClient;
    private String codeCifBank;
    private String passport;
    private Long civilStatus;
    private Long nationality;
    private Long licenseType;
    private String licenseNumber;
    private String email;

    private String addressHome;
    private Long countryHome;
    private Long departmentHome;
    private Long municipalityHome;
    private Long zoneHome;
    private String phone1Home;
    private String phone2Home;
    private String phone3Home;
    private String colonyHome;

    private String addressWork;
    private Long countryWork;
    private Long departmentWork;
    private Long municipalityWork;
    private Long zoneWork;
    private String phone1Work;
    private String phone2Work;
    private String phone3Work;

    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getFirstSurname() {
        return firstSurname;
    }

    public void setFirstSurname(String firstSurname) {
        this.firstSurname = firstSurname;
    }

    public String getSecondSurname() {
        return secondSurname;
    }

    public void setSecondSurname(String secondSurname) {
        this.secondSurname = secondSurname;
    }

    public String getMarriedSurname() {
        return marriedSurname;
    }

    public void setMarriedSurname(String marriedSurname) {
        this.marriedSurname = marriedSurname;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public Long getSex() {
        return sex;
    }

    public void setSex(Long sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Long getProfession() {
        return profession;
    }

    public void setProfession(Long profession) {
        this.profession = profession;
    }

    public String getIdentificationDocument() {
        return identificationDocument;
    }

    public void setIdentificationDocument(String identificationDocument) {
        this.identificationDocument = identificationDocument;
    }

    public String getCodeClient() {
        return codeClient;
    }

    public void setCodeClient(String codeClient) {
        this.codeClient = codeClient;
    }

    public String getCodeCifBank() {
        return codeCifBank;
    }

    public void setCodeCifBank(String codeCifBank) {
        this.codeCifBank = codeCifBank;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public Long getCivilStatus() {
        return civilStatus;
    }

    public void setCivilStatus(Long civilStatus) {
        this.civilStatus = civilStatus;
    }

    public Long getNationality() {
        return nationality;
    }

    public void setNationality(Long nationality) {
        this.nationality = nationality;
    }

    public Long getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(Long licenseType) {
        this.licenseType = licenseType;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddressHome() {
        return addressHome;
    }

    public void setAddressHome(String addressHome) {
        this.addressHome = addressHome;
    }

    public Long getCountryHome() {
        return countryHome;
    }

    public void setCountryHome(Long countryHome) {
        this.countryHome = countryHome;
    }

    public Long getDepartmentHome() {
        return departmentHome;
    }

    public void setDepartmentHome(Long departmentHome) {
        this.departmentHome = departmentHome;
    }

    public Long getMunicipalityHome() {
        return municipalityHome;
    }

    public void setMunicipalityHome(Long municipalityHome) {
        this.municipalityHome = municipalityHome;
    }

    public Long getZoneHome() {
        return zoneHome;
    }

    public void setZoneHome(Long zoneHome) {
        this.zoneHome = zoneHome;
    }

    public String getPhone1Home() {
        return phone1Home;
    }

    public void setPhone1Home(String phone1Home) {
        this.phone1Home = phone1Home;
    }

    public String getPhone2Home() {
        return phone2Home;
    }

    public void setPhone2Home(String phone2Home) {
        this.phone2Home = phone2Home;
    }

    public String getPhone3Home() {
        return phone3Home;
    }

    public void setPhone3Home(String phone3Home) {
        this.phone3Home = phone3Home;
    }

    public String getColonyHome() {
        return colonyHome;
    }

    public void setColonyHome(String colonyHome) {
        this.colonyHome = colonyHome;
    }

    public String getAddressWork() {
        return addressWork;
    }

    public void setAddressWork(String addressWork) {
        this.addressWork = addressWork;
    }

    public Long getCountryWork() {
        return countryWork;
    }

    public void setCountryWork(Long countryWork) {
        this.countryWork = countryWork;
    }

    public Long getDepartmentWork() {
        return departmentWork;
    }

    public void setDepartmentWork(Long departmentWork) {
        this.departmentWork = departmentWork;
    }

    public Long getMunicipalityWork() {
        return municipalityWork;
    }

    public void setMunicipalityWork(Long municipalityWork) {
        this.municipalityWork = municipalityWork;
    }

    public Long getZoneWork() {
        return zoneWork;
    }

    public void setZoneWork(Long zoneWork) {
        this.zoneWork = zoneWork;
    }

    public String getPhone1Work() {
        return phone1Work;
    }

    public void setPhone1Work(String phone1Work) {
        this.phone1Work = phone1Work;
    }

    public String getPhone2Work() {
        return phone2Work;
    }

    public void setPhone2Work(String phone2Work) {
        this.phone2Work = phone2Work;
    }

    public String getPhone3Work() {
        return phone3Work;
    }

    public void setPhone3Work(String phone3Work) {
        this.phone3Work = phone3Work;
    }

    public static PersonDetailDTO loadData(QueryPersonDetailResponse personDetail) {
        PersonDetailDTO dto = new PersonDetailDTO();
        dto.setTaxNumber(personDetail.getTaxNumber());
        dto.setFirstName(personDetail.getFirstName());
        dto.setSecondName(personDetail.getSecondName());
        dto.setFirstSurname(personDetail.getFirstSurname());
        dto.setSecondSurname(personDetail.getSecondSurname());
        dto.setMarriedSurname(personDetail.getMarriedSurname());
        dto.setBirthdate(personDetail.getBirthdate());
        dto.setSex(ER_Sex.getObjectId(personDetail.getSex()));
        dto.setAge(personDetail.getAge());
        dto.setProfession(ER_Profession.getObjectId(personDetail.getProfession()));
        dto.setIdentificationDocument(personDetail.getIdentificationDocument());
        dto.setCodeClient(personDetail.getCodeClient());
        dto.setCodeCifBank(personDetail.getCodeCifBank());
        dto.setPassport(personDetail.getPassport());
        dto.setCivilStatus(ER_Civil_Status.getObjectId(personDetail.getCivilStatus()));
        dto.setNationality(ER_Nationality.getObjectId(personDetail.getNationality()));
        dto.setLicenseType(ER_License_Type.getObjectId(personDetail.getLicenseType()));
        dto.setLicenseNumber(personDetail.getLicenseNumber());
        dto.setEmail(personDetail.getEmail());

        if (personDetail.getAddress() != null) {
            if (personDetail.getAddress().getHomeAddress() != null) {
                dto.setAddressHome(personDetail.getAddress().getHomeAddress().getAddress());
                dto.setCountryHome(ER_Geographic_Area.getObjectId(personDetail.getAddress().getHomeAddress().getCountry()));
                dto.setDepartmentHome(ER_Geographic_Area.getObjectId(personDetail.getAddress().getHomeAddress().getDepartment()));
                dto.setMunicipalityHome(ER_Geographic_Area.getObjectId(personDetail.getAddress().getHomeAddress().getMunicipality()));
                dto.setZoneHome(ER_Geographic_Area.getObjectId(personDetail.getAddress().getHomeAddress().getZone()));
                dto.setPhone1Home(personDetail.getAddress().getHomeAddress().getPhone1());
                dto.setPhone2Home(personDetail.getAddress().getHomeAddress().getPhone2());
                dto.setPhone3Home(personDetail.getAddress().getHomeAddress().getPhone3());
                dto.setColonyHome(personDetail.getAddress().getHomeAddress().getColony());
            }
            if (personDetail.getAddress().getWorkAddress() != null) {
                dto.setAddressWork(personDetail.getAddress().getWorkAddress().getAddress());
                dto.setCountryWork(ER_Geographic_Area.getObjectId(personDetail.getAddress().getWorkAddress().getCountry()));
                dto.setDepartmentWork(ER_Geographic_Area.getObjectId(personDetail.getAddress().getWorkAddress().getDepartment()));
                dto.setMunicipalityWork(ER_Geographic_Area.getObjectId(personDetail.getAddress().getWorkAddress().getMunicipality()));
                dto.setZoneWork(ER_Geographic_Area.getObjectId(personDetail.getAddress().getWorkAddress().getZone()));
                dto.setPhone1Work(personDetail.getAddress().getWorkAddress().getPhone1());
                dto.setPhone2Work(personDetail.getAddress().getWorkAddress().getPhone2());
                dto.setPhone3Work(personDetail.getAddress().getWorkAddress().getPhone3());
            }
        }
        return dto;
    }
}
