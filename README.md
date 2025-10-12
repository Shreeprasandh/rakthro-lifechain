# RAKTHRO - Blood Donor Management System

## Overview

RAKTHRO is a comprehensive Java-based console application designed to streamline blood donation management in Tamil Nadu, India. The system facilitates donor registration, appointment scheduling, blood request handling, and specialized disease-based donation programs for Thalassemia and Sickle Cell Anemia patients. Built with a focus on user-friendly interfaces and robust data management, RAKTHRO serves as a bridge between blood donors and recipients through efficient coordination and communication.

## Features

### Core Functionality
- **Donor Management**: Complete donor lifecycle management from registration to donation tracking
- **Appointment System**: Automated appointment booking with hospital selection and email confirmations
- **Blood Request Portal**: Emergency blood request submission and fulfillment coordination
- **Authentication System**: Secure login for admins and users with multiple authentication methods
- **Email Notifications**: Automated email confirmations for appointments and updates

### Specialized Disease Support
- **Thalassemia Program**: Dedicated support for Thalassemia patients including checkups, donations, and request matching
- **Sickle Cell Anemia Program**: Comprehensive SCD management with transfusion scheduling and donor matching
- **Disease-Based Dashboards**: Analytics and insights for disease-specific donor pools

### Administrative Features
- **Admin Panel**: Full system oversight with donor management, appointment viewing, and request monitoring
- **Donor Analytics**: City-wise and blood group-wise donor distribution analysis
- **Donation History Tracking**: Complete audit trail of all donations and appointments

### User Experience
- **Multi-Role Access**: Separate interfaces for administrators and regular users
- **Interactive Menus**: Intuitive console-based navigation system
- **Data Validation**: Comprehensive input validation and error handling
- **Real-time Updates**: Live data synchronization across all system components

## System Architecture

### Technology Stack
- **Language**: Java (JDK 8+)
- **Data Storage**: CSV-based file system for portability and simplicity
- **Email Service**: JavaMail API with Gmail SMTP integration
- **Build System**: Manual compilation with JAR dependencies

### Design Patterns
- **Manager Classes**: Centralized business logic management (DonorManager, ThalassemiaManager, etc.)
- **Data Access Objects**: CSV file handling with buffered I/O operations
- **Factory Pattern**: Dynamic ID generation for donors and appointments
- **Observer Pattern**: Email notification system integration

## Installation

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Windows/Linux/Mac OS with command line access
- Internet connection for email functionality

### Setup Steps

1. **Clone or Download the Project**
   ```bash
   # Navigate to the project directory
   cd c:/Shreeprasandh/personal/create_shyt/Rakthro/Base_PROTOTYPE
   ```

2. **Compile the Source Code**
   ```bash
   # Compile all Java files
   javac -cp "lib/*" src/*.java -d bin/
   ```

3. **Set Classpath and Run**
   ```bash
   # Run the application
   java -cp "bin:lib/*" Main
   ```

### Dependencies
- **activation-1.1.1.jar**: JavaBeans Activation Framework
- **gson-2.8.9.jar**: JSON processing library
- **javax.mail-1.6.2.jar**: JavaMail API for email functionality

## Usage Guide

### System Access

#### Admin Login
1. Start the application
2. Select option "1. Admin Login"
3. Enter credentials from `db/admin.csv`
4. Access administrative features

#### User Access
1. Select option "2. User Login"
2. Choose "Log In" or "Sign In"
3. For Log In: Use Donor ID + Password or Username + Email + Password
4. For Sign In: Register as new donor

### Donor Registration Flow

#### New Donor Registration
1. Access User Panel → Register Donor
2. Enter personal details:
   - Name (required)
   - Age (18-65 years)
   - Blood Group (A+, A-, B+, B-, AB+, AB-, O+, O-)
   - City (Tamil Nadu cities)
   - Contact (10-digit number)
   - Email (valid email address)
   - Last Donated Date (YYYY-MM-DD or N/A)
3. System generates unique Donor ID (D001, D002, etc.)
4. Automatic registration in Thalassemia database

#### Eligibility Criteria
- Age: 18-65 years
- Valid blood group selection
- Complete contact information
- 90-day gap between donations (enforced)

### Appointment Management

#### Booking Appointments
1. User Panel → Donation Appointment → Book New Appointment
2. Enter Donor ID for verification
3. System checks eligibility (90-day rule)
4. Select city and hospital from directory
5. System generates appointment slip
6. Email confirmation sent automatically

#### Appointment Features
- Random date generation (1-7 days ahead)
- Time slots: 9:00 AM - 12:00 PM
- Hospital selection from predefined directory
- PDF-style appointment slips saved to `appointments/` folder
- Email notifications with full details

#### Cancellation Process
1. Select "Cancel Appointment"
2. Enter Donor ID
3. View existing appointments
4. Select appointment to cancel
5. Confirmation email sent

### Blood Request System

#### Request Submission
1. User Panel → Blood Request
2. Enter request details:
   - Name
   - City
   - Blood Group
   - Contact
   - Email
3. Request stored in appropriate CSV file

#### Request Fulfillment
1. Admin/User views pending requests
2. Select request to fulfill
3. Enter Donor ID for matching
4. System validates donor eligibility
5. Select hospital and schedule appointment
6. Automatic email notifications
7. Request marked as fulfilled

### Disease-Based Donation Programs

#### Thalassemia Support
1. **Registration**: Automatic when registering as general donor
2. **Checkup Booking**:
   - Book thalassemia-specific checkups
   - Hospital selection
   - Email confirmations
3. **Donation Process**:
   - Eligibility check based on thalassemia type
   - Hospital appointment booking
   - Donation logging
4. **Request Matching**: View and accept thalassemia blood requests
5. **Dashboard**: City-wise and type-wise donor analytics

#### Sickle Cell Anemia Support
1. **Registration**: Dedicated SCD donor registration
2. **Transfusion Scheduling**: Book transfusion appointments
3. **Checkup Management**: Regular health checkup booking
4. **History Tracking**: Complete transfusion and donation history
5. **Dashboard**: SCD-specific donor analytics

### Administrative Functions

#### Donor Management
- View all donors with complete details
- Search donors by ID, blood group, or city
- Edit donor information (city, contact)
- Delete donor records
- View donors by last donation date

#### System Monitoring
- View all appointments across the system
- Monitor blood requests by city and blood group
- Access donor dashboards and analytics
- Manage system-wide notifications

## Data Flow and Storage

### CSV Database Structure

#### Core Files
- `db/donors.csv`: Main donor registry
- `db/user.csv`: User authentication data
- `db/admin.csv`: Administrative credentials
- `db/hospital.csv`: Hospital directory by city

#### Appointment Files
- `db/appointments.csv`: General appointment logs
- `db/appointments_simple.csv`: Simplified appointment tracking
- `appointments/DXXX_appointment.txt`: Individual appointment slips

#### Disease-Specific Files
- `db/thal_donors.csv`: Thalassemia donor database
- `db/thal_checkups.csv`: Thalassemia checkup records
- `db/thal_donations.csv`: Thalassemia donation history
- `db/thal_blood_requests.csv`: Thalassemia blood requests
- `db/sickle_donors.csv`: Sickle Cell donor database
- `db/sickle_checkups.csv`: SCD checkup records
- `db/sickle_donations.csv`: SCD donation history
- `db/sickle_transfusions.csv`: SCD transfusion logs

#### Request Files
- `db/blood_requests.csv`: General blood requests
- `db/requests.csv`: Generated request analytics

### Data Relationships
- Donor ID (DXXX) links across all donor-related files
- Thal ID (TXXX) and SCD ID (SCXXX) link to main Donor ID
- Email addresses used for notifications and authentication
- City-based hospital mapping for appointment scheduling

## Class Architecture

### Core Classes

#### Main.java
- Application entry point
- Main menu navigation
- User/Admin role routing

#### DonorManager.java
- Central donor operations
- Registration, search, editing
- Appointment management
- Eligibility checking
- Dashboard analytics

#### AuthSystem.java
- Authentication handling
- Admin and user login validation
- Credential verification

#### DiseaseDonationManager.java
- Disease-specific menu routing
- Thalassemia and SCD coordination

### Specialized Managers

#### ThalassemiaManager.java
- Thalassemia donor management
- Checkup and donation scheduling
- Request matching and fulfillment
- Thalassemia-specific analytics

#### SickleCellManager.java
- SCD donor operations
- Transfusion appointment handling
- Checkup management
- SCD analytics and reporting

#### BloodRequestManager.java
- Blood request processing
- Request fulfillment coordination
- Multi-type request handling (normal, thal, sickle)

### Utility Classes

#### MailService.java
- Email notification system
- SMTP configuration
- Template-based messaging

#### HospitalDirectory.java
- Hospital data management
- City-based hospital lookup
- Dynamic hospital loading

#### Donor Classes
- `Donor.java`: General donor model
- `ThalassemiaDonor.java`: Thalassemia-specific donor
- `SickleCellDonor.java`: SCD donor model
- `BloodRequest.java`: Request data model

## Email Integration

### Configuration
- Gmail SMTP server (smtp.gmail.com:587)
- TLS encryption enabled
- App password authentication

### Email Templates
- **Appointment Confirmation**: Date, time, hospital details
- **Cancellation Notice**: Appointment cancellation confirmation
- **Disease-Specific Notifications**: Thalassemia/SCD specific messaging
- **Request Fulfillment**: Donor matching notifications

### Email Flow
1. User provides email during registration/appointment
2. System generates personalized content
3. JavaMail API handles SMTP transmission
4. Success/failure logging to console

## Security Features

### Authentication
- Multi-factor credential validation
- Secure password storage (plaintext for demo)
- Role-based access control
- Session-based menu access

### Data Validation
- Input sanitization
- Format validation (dates, emails, phone numbers)
- Range checking (age, blood groups)
- Duplicate prevention

### File Security
- CSV file access controls
- Backup prevention through file locking
- Error handling for file operations

## System Limitations and Future Enhancements

### Current Limitations
- Console-based interface (no GUI)
- CSV storage (not suitable for large-scale deployment)
- Email dependency on Gmail SMTP
- No real-time notifications
- Limited concurrent user support

### Potential Enhancements
- Web-based interface with React/Vue.js
- Database migration (MySQL/PostgreSQL)
- Mobile app development
- Real-time notification system
- Advanced analytics dashboard
- Multi-language support
- Integration with hospital systems
- Blood bank inventory management

## Troubleshooting

### Common Issues

#### Compilation Errors
```bash
# Ensure classpath includes all JARs
javac -cp "lib/*" src/*.java -d bin/
```

#### Email Not Sending
- Verify Gmail app password
- Check internet connection
- Confirm recipient email format

#### File Access Errors
- Ensure write permissions on db/ and appointments/ folders
- Check CSV file formats
- Verify file paths

#### Authentication Failures
- Check admin.csv and user.csv formats
- Verify password entries
- Ensure proper CSV encoding

## Contributing

### Development Setup
1. Fork the repository
2. Create feature branch
3. Make changes following existing patterns
4. Test thoroughly
5. Submit pull request

### Code Standards
- Follow Java naming conventions
- Add comprehensive comments
- Maintain CSV data integrity
- Test all user flows

## License

This project is developed for educational and humanitarian purposes. All rights reserved.

## Support

For technical support or feature requests, please contact the development team or create an issue in the project repository.

---

**RAKTHRO** - Connecting donors with those in need, one drop at a time.

# Author
**Shreeprasandh K**