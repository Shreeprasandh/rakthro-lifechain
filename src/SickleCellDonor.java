public class SickleCellDonor {
    public String scdId;
    public String donorId;
    public String name;
    public int age;
    public String bloodGroup;
    public String city;
    public String contact;
    public String email;
    public String scdType; // e.g., SS, SC, Sβ+
    public String scdStatus; // e.g., Active/Under Observation
    public String lastTransfusionDate;
    public String eligibleToDonateSCD;

    public SickleCellDonor(String scdId, String donorId, String name, int age, String bloodGroup, String city, String contact,
                        String email, String scdType, String scdStatus, String lastTransfusionDate,
                        String eligibleToDonateSCD) {
    this.scdId = scdId;
    this.donorId = donorId;
    this.name = name;
    this.age = age;
    this.bloodGroup = bloodGroup;
    this.city = city;
    this.contact = contact;
    this.email = email;
    this.scdType = scdType;
    this.scdStatus = scdStatus;
    this.lastTransfusionDate = lastTransfusionDate;
    this.eligibleToDonateSCD = eligibleToDonateSCD;
}


    public String toCSV() {
    return scdId + "," + donorId + "," + name + "," + age + "," + bloodGroup + "," + city + "," + contact + "," +
           email + "," + scdType + "," + scdStatus + "," + lastTransfusionDate + "," +
           eligibleToDonateSCD;
}


    public static SickleCellDonor fromCSV(String line) {
    String[] parts = line.split(",");
    if (parts.length != 12) return null;

    return new SickleCellDonor(
        parts[0].trim(), // scdId
        parts[1].trim(), // donorId
        parts[2].trim(), // name
        Integer.parseInt(parts[3].trim()), // age
        parts[4].trim(), parts[5].trim(), parts[6].trim(),
        parts[7].trim(), parts[8].trim(), parts[9].trim(),
        parts[10].trim(), parts[11].trim() // lastTransfusionDate, eligibleToDonateSCD
    );
}

}
