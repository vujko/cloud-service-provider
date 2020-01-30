package controllers;

import main.App;
import model.Drive;
import model.Organization;
import model.User;
import model.VirtualMachine;

public class ReferenceController {

    public static void setUpReferences(){
        connectUsersWithOrganizations();
        connectMachinesWithDrives();
        connectOrganizationsWithMachines();
        connectOrganizationsWithDrives();
    }

    private static void connectUsersWithOrganizations(){
        for (User user : App.userService.getUsers()) {
            for(Organization org : App.orgService.getOrganizations()){
                if(user.getOrganization().getName().equalsIgnoreCase(org.getName())){
                    user.setOrganization(org);
                    org.addUser(user);
                }
            }
        }
    }

    private static void connectMachinesWithDrives(){
        for(VirtualMachine vm : App.machineService.getMachines()){
            for(Drive d : App.driveService.getDrives()){
                String machineName = d.getVm().getName();
                if(machineName != null && machineName.equals(vm.getName())){
                    vm.addDrive(d);
                    d.setVm(vm);
                }
            }
        }
    }

    private static void connectOrganizationsWithMachines(){
        for(Organization org : App.orgService.getOrganizations()){
            for(VirtualMachine vm : App.machineService.getMachines()){
                if(vm.getOrganization().getName().equals(org.getName())){
                    vm.setOrganization(org);
                    org.addMachine(vm);

                }
            }
        }
    }

    private static void connectOrganizationsWithDrives(){
        for(Organization org : App.orgService.getOrganizations()){
            for(Drive d : App.driveService.getDrives()){
                if(d.getOrganization().getName().equals(org.getName())){
                    d.setOrganization(org);
                    org.addDrive(d);
                }
            }
        }
    }



    
}