public class Donor {
    String donorId;
    String name;
    int age;
    String bloodGroup;
    String city;
    String contact;
    String lastDonatedDate;
    String email;



    public Donor(String donorId, String name, int age, String bloodGroup, String city, String contact, String lastDonatedDate) {
    this.donorId = donorId;
    this.name = name;
    this.age = age;
    this.bloodGroup = bloodGroup;
    this.city = city;
    this.contact = contact;
    this.lastDonatedDate = lastDonatedDate;
}

    public String getLastDonatedDate() {
        return lastDonatedDate;
    }
    public void setLastDonatedDate(String date) {
    this.lastDonatedDate = date;
    }
    public String getDonorId() {
    return donorId;
    }
    public void setDonorId(String id) {
    this.donorId = id;
    }
    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
    public String getId() {
        return donorId;
    }

    public int getAge() {
        return age;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public String getCity() {
        return city;
    }

    public String getContact() {
        return contact;
    }





    @Override
    public String toString() {
    String donateInfo = (lastDonatedDate == null || lastDonatedDate.equalsIgnoreCase("N/A") || lastDonatedDate.isBlank())
        ? "No donation yet"
        : "Last Donated: " + lastDonatedDate;

    return donorId + " - " + name + ", " + age + " yrs, BG: " + bloodGroup + ", City: " + city + ", Contact: " + contact + ", " + donateInfo;
}


    public String toCSV() {
    return donorId + "," + name + "," + age + "," + bloodGroup + "," + city + "," + contact + "," + lastDonatedDate;
}

    public static Donor fromCSV(String line) {
    String[] parts = line.split(",");
    if (parts.length != 7) return null;

    return new Donor(
        parts[0].trim(),  // Donor ID
        parts[1].trim(),  // Name
        Integer.parseInt(parts[2].trim()),  // Age
        parts[3].trim(),  // Blood Group
        parts[4].trim(),  // City
        parts[5].trim(),  // Contact
        parts[6].trim()   // Last Donated Date
    );
}

}


