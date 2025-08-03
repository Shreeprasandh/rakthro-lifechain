public class ThalassemiaDonor {
    String donorId;
    String thalId;
    String name;
    int age;
    String bloodGroup;
    String city;
    String contact;
    String thalType;
    String thalStatus;
    String lastCheckupDate;
    String lastThalDonationDate;
    public String eligibleToDonateThal;
    public String email;
    String checkupHospital;
    String thalRemarks;

    public ThalassemiaDonor(String thalId, String donorId, String name, int age, String bloodGroup, String city, String contact,
                        String email, String thalType, String thalStatus, String lastCheckupDate,
                        String lastThalDonationDate, String eligibleToDonateThal,
                        String checkupHospital, String thalRemarks) {
    this.thalId = thalId;
    this.donorId = donorId;
    this.name = name;
    this.age = age;
    this.bloodGroup = bloodGroup;
    this.city = city;
    this.contact = contact;
    this.email = email;
    this.thalType = thalType;
    this.thalStatus = thalStatus;
    this.lastCheckupDate = lastCheckupDate;
    this.lastThalDonationDate = lastThalDonationDate;
    this.eligibleToDonateThal = eligibleToDonateThal;
    this.checkupHospital = checkupHospital;
    this.thalRemarks = thalRemarks;
}


    public String toCSV() {
    return thalId + "," + donorId + "," + name + "," + age + "," + bloodGroup + "," + city + "," + contact + "," + email + "," +
           thalType + "," + thalStatus + "," + lastCheckupDate + "," +
           lastThalDonationDate + "," + eligibleToDonateThal + "," +
           checkupHospital + "," + thalRemarks;
}


    public static ThalassemiaDonor fromCSV(String line) {
    String[] parts = line.split(",");
    if (parts.length != 15) return null;

    return new ThalassemiaDonor(
        parts[0].trim(), // thalId
        parts[1].trim(), // donorId
        parts[2].trim(), // name
        Integer.parseInt(parts[3].trim()), // age
        parts[4].trim(), parts[5].trim(), parts[6].trim(),
        parts[7].trim(), parts[8].trim(), parts[9].trim(), parts[10].trim(),
        parts[11].trim(), parts[12].trim(), parts[13].trim(),parts[14].trim()
    );
}


}
