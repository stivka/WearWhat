package door;

import securitysystem.VerificationClass;

public class DoorLock {

    private String schoolName;
    private VerificationClass accessService;

    public void verifyAccess(String identificationCode) {
        boolean accessGranted = accessService.checkAccessRights(identificationCode, schoolName);
        System.out.println("Student: "
                + identificationCode
                + " from "
                + schoolName
                + " , access granted: "
                + accessGranted);
    }

    public DoorLock(String schoolName) {
        this.schoolName = schoolName;
        this.accessService = new Veri();
    }
}