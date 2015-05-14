package org.benjamin.ci.modules.vijava;

import java.rmi.RemoteException;

import com.vmware.vim25.InvalidState;
import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.TaskInProgress;
import com.vmware.vim25.VirtualMachineSnapshotTree;
import com.vmware.vim25.mo.ServerConnection;
import com.vmware.vim25.mo.VirtualMachine;
import com.vmware.vim25.mo.VirtualMachineSnapshot;

public enum VMSnapshotAction {
	DELETE {
		@Override
		public void execute(VirtualMachine vm, ServerConnection sc)
				throws TaskInProgress, InvalidState, RuntimeFault,
				RemoteException {
			VirtualMachineSnapshot snapshot = this.getVirtualMachineSnapshot(vm, sc);
			snapshot.removeSnapshot_Task(false);
		}
	},REVERT {
		@Override
		public void execute(VirtualMachine vm, ServerConnection sc)
				throws TaskInProgress, InvalidState, RuntimeFault,
				RemoteException {
			VirtualMachineSnapshot snapshot = this.getVirtualMachineSnapshot(vm, sc);
			snapshot.revertToSnapshot_Task(null);
		}
	},CREATE {
		@Override
		public void execute(VirtualMachine vm, ServerConnection sc)
				throws TaskInProgress, InvalidState, RuntimeFault,
				RemoteException {
			vm.createSnapshot_Task(snapshotName, description, false, false);
		}
	};
	
	String snapshotName;
	String description;
	public void setSnapshotName(String snapshotName) {
		this.snapshotName = snapshotName;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	protected VirtualMachineSnapshot getVirtualMachineSnapshot(VirtualMachine vm, ServerConnection sc){
		VirtualMachineSnapshotTree[] rootSnapshotList = vm.getSnapshot().getRootSnapshotList();
		VirtualMachineSnapshotTree snapshotTree = findInSnapshotTree(snapshotName, rootSnapshotList);
		return new VirtualMachineSnapshot(sc, snapshotTree.getSnapshot());
	}
	
	private VirtualMachineSnapshotTree findInSnapshotTree(String snapshotName, VirtualMachineSnapshotTree[] rootSnapshotList) {
		if (rootSnapshotList != null) {
			for (int i = 0; i < rootSnapshotList.length; i++) {
				VirtualMachineSnapshotTree virtualMachineSnapshotTree = rootSnapshotList[i];
				String name = virtualMachineSnapshotTree.getName();
				System.out.println("checking " + name);
				if (snapshotName.equals(name)) {
					return virtualMachineSnapshotTree;
				} else {
					VirtualMachineSnapshotTree snapshot = findInSnapshotTree(snapshotName, virtualMachineSnapshotTree.childSnapshotList);
					if (snapshot != null) {
						return snapshot;
					}
				}
			}
		}
		return null;
	}
	
	public abstract void execute(VirtualMachine vm, ServerConnection sc) throws TaskInProgress, InvalidState, RuntimeFault, RemoteException;
	
}
