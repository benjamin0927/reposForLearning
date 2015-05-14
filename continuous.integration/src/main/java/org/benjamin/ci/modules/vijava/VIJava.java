package org.benjamin.ci.modules.vijava;

import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Properties;

import com.vmware.vim25.GuestInfo;
import com.vmware.vim25.InvalidProperty;
import com.vmware.vim25.ManagedEntityStatus;
import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.VirtualDevice;
import com.vmware.vim25.VirtualEthernetCard;
import com.vmware.vim25.VirtualHardware;
import com.vmware.vim25.VirtualMachineConfigInfo;
import com.vmware.vim25.VirtualMachineQuickStats;
import com.vmware.vim25.VirtualMachineSummary;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;

/**
 * https://github.com/zanox/viAutomator
 * http://benohead.com/getting-info-from-your-esx-server-using-the-vmware-infrastructure-vsphere-java-api-part-1/
 * http://benohead.com/getting-info-from-your-esx-server-using-the-vmware-infrastructure-vsphere-java-api-part-2/
 * http://benohead.com/getting-info-from-your-esx-server-using-the-vmware-infrastructure-vsphere-java-api-part-3/
 * http://benohead.com/getting-info-from-your-esx-server-using-the-vmware-infrastructure-vsphere-java-api-part-4/
 * http://benohead.com/vi-java-api-performance-historical-intervals/
 * 
 * http://www.infoq.com/presentations/vsphere-java-api
 * http://www.imchris.org/wp/2012/02/13/vmware-vsphere-java-examples/
 */

public class VIJava {
	private static String SERVER_NAME = "";
	private static String USER_NAME = "";
	private static String PASSWORD = "";
	String url = "https://" + SERVER_NAME + "/sdk/vimService";
	private static ServiceInstance si;
	
	public VIJava() throws RemoteException, MalformedURLException{
		
		Properties pro = new Properties();
		try {
			pro.load(new FileInputStream(new File(VIJava.class.getClassLoader().getResource("vm.properties").getFile())));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		SERVER_NAME = pro.getProperty("server.name");
		USER_NAME = pro.getProperty("user.name");
		PASSWORD = pro.getProperty("user.password");
		url = "https://" + SERVER_NAME + "/sdk/vimService";

		si = new ServiceInstance(new URL(url), USER_NAME, PASSWORD, true);	
	}
	
	public VirtualMachine getVMByName(final String vmName) throws InvalidProperty, RuntimeFault, RemoteException{
		 Folder rootFolder = si.getRootFolder();
         return (VirtualMachine) new InventoryNavigator(rootFolder).searchManagedEntity("VirtualMachine", vmName);
	}
	
	public Boolean isVMNameAvailable(final String vmName) throws InvalidProperty, RuntimeFault, RemoteException{
         VirtualMachine vm = this.getVMByName(vmName);
         if (vm == null)
             return Boolean.TRUE;
         else
             return Boolean.FALSE;
	}
	
	public ManagedEntityStatus getStatusForVm(final String vmName) throws InvalidProperty, RuntimeFault, RemoteException{
        VirtualMachine vm = this.getVMByName(vmName);
        return vm.getOverallStatus();
	}
	
	public VirtualMachineConfigInfo getConfigInfoForVM(final String vmName) throws InvalidProperty, RuntimeFault, RemoteException{
        VirtualMachine vm = this.getVMByName(vmName);
        return vm.getConfig();
	}
	
	public VirtualHardware getHardWareForVM(final String vmName) throws InvalidProperty, RuntimeFault, RemoteException{
        return this.getConfigInfoForVM(vmName).getHardware();
	}
	
	public GuestInfo getGuestInfoForVM(final String vmName) throws InvalidProperty, RuntimeFault, RemoteException{
        return this.getVMByName(vmName).getGuest();
	}
	
	public VirtualMachineSummary getVirtualMachineSummaryForVM(final String vmName) throws InvalidProperty, RuntimeFault, RemoteException{
        return this.getVMByName(vmName).getSummary();
	}
	
	public VirtualMachineQuickStats getVirtualMachineQuickStatsForVM(final String vmName) throws InvalidProperty, RuntimeFault, RemoteException{
        return this.getVirtualMachineSummaryForVM(vmName).getQuickStats();
	}
	
	public void doVMAction(final String vmName, VMAction vmAction) throws InvalidProperty, RuntimeFault, RemoteException {
		VirtualMachine vm = this.getVMByName(vmName);
		vmAction.run(vm);
	}
	
	public void doVMSnapshotAction(final String vmName, VMSnapshotAction vmSnapshotAction,
			String snapshotName, String description) throws InvalidProperty, RuntimeFault, RemoteException {
		VirtualMachine vm = this.getVMByName(vmName);
		vmSnapshotAction.setSnapshotName(snapshotName);
		vmSnapshotAction.setDescription(description);
		vmSnapshotAction.execute(vm, si.getServerConnection());
	}
	
	public String getMacAddressForVm(final String vmName) throws InvalidProperty, RuntimeFault, RemoteException{
		 VirtualDevice[] devices = this.getHardWareForVM(vmName).getDevice();
         VirtualEthernetCard vec = null;
         for (VirtualDevice dev : devices) {
             if (dev instanceof VirtualEthernetCard) {
                 vec = (VirtualEthernetCard) dev;
                 break;
             }
         }
         return vec.getMacAddress();
	}
	
	public void listFoldersAndVMsInFolder(String folderName) throws InvalidProperty, RuntimeFault, RemoteException{
		Folder folder = (Folder)new InventoryNavigator(si.getRootFolder()).searchManagedEntity("Folder", folderName);
		this.listFoldersAndVMs(folder, 0);
	}
	
	public void listFoldersAndVMs(Folder folder, int depth) throws InvalidProperty, RuntimeFault, RemoteException{
        for(ManagedEntity folderChildEntity : folder.getChildEntity()){
        	if(folderChildEntity instanceof Folder) {
        		Folder subFolder = (Folder)folderChildEntity;
        		System.out.println(this.getMultipleTimesString("----", depth) + "| " + subFolder.getName());
        		
        		this.listFoldersAndVMs(subFolder, depth+1);
        		
        	} else {
        		System.out.println(this.getMultipleTimesString("----", depth) + "| " + folderChildEntity.getName() + " |");
        	}
        }
	}
	
	public StringBuffer getMultipleTimesString(String str, int times){
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<=times; i++) {
			sb.append(str);
		}
		return sb;
	}
	

	public static void main(String[] args) {
		try {
			VIJava vm = new VIJava();
			
			vm.listFoldersAndVMsInFolder("Folder");
			System.out.println(vm.getStatusForVm("VM_Name"));
			
			VMAction vmOperation = VMAction.POWER_OFF;
			vm.doVMAction("VM_Name", vmOperation);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
}
