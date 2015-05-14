package org.benjamin.ci.modules.vijava;

import java.rmi.RemoteException;

import com.vmware.vim25.FileFault;
import com.vmware.vim25.InsufficientResourcesFault;
import com.vmware.vim25.InvalidState;
import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.TaskInProgress;
import com.vmware.vim25.ToolsUnavailable;
import com.vmware.vim25.VmConfigFault;
import com.vmware.vim25.mo.Task;
import com.vmware.vim25.mo.VirtualMachine;


public enum VMAction {
	POWER_OFF{
		@Override
		public void execute(VirtualMachine vm) throws TaskInProgress, InvalidState, RuntimeFault, RemoteException {
			task = vm.powerOffVM_Task();
		}
	},
	POWER_ON {
		@Override
		public void execute(VirtualMachine vm) throws VmConfigFault, TaskInProgress, FileFault, InvalidState, InsufficientResourcesFault, RuntimeFault, RemoteException {
			task = vm.powerOnVM_Task(null);
		}
	},
	REBOOT {
		@Override
		public void execute(VirtualMachine vm) throws TaskInProgress, InvalidState, ToolsUnavailable, RuntimeFault, RemoteException {
			vm.rebootGuest();
		}
	},
	RESET {
		@Override
		public void execute(VirtualMachine vm) throws TaskInProgress, InvalidState, RuntimeFault, RemoteException {
			 task = vm.resetVM_Task();
		}
	},
	SHUTDOWN {
		@Override
		public void execute(VirtualMachine vm) throws TaskInProgress, InvalidState, ToolsUnavailable, RuntimeFault, RemoteException {
			vm.shutdownGuest();
		}
	},
	STANDBY {
		@Override
		public void execute(VirtualMachine vm) throws TaskInProgress, InvalidState, ToolsUnavailable, RuntimeFault, RemoteException {
			vm.standbyGuest();
		}
	},
	SUSPEND {
		@Override
		public void execute(VirtualMachine vm) throws TaskInProgress, InvalidState, RuntimeFault, RemoteException {
			vm.suspendVM_Task();
		}
	};
	
	Task task = null;
	public abstract void execute(VirtualMachine vm) throws TaskInProgress, InvalidState, RuntimeFault, RemoteException;
	
	public void run(VirtualMachine vm){
		boolean retVal = false;
		try {
			this.execute(vm);
            String result = Task.SUCCESS;

            if (task != null)
                task.waitForTask();

            retVal = result.equals(Task.SUCCESS);
            
            System.out.println(this + " - " + retVal);
		} catch (Exception e) {
			if(this == VMAction.SHUTDOWN){
				this.run(vm);
			}
			e.printStackTrace();
		}
	}
	
}
