
package net.sourceforge.filebot.ui.transfer;


import java.awt.datatransfer.Transferable;
import java.awt.dnd.InvalidDnDOperationException;

import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;

import net.sourceforge.filebot.ui.transferablepolicies.TransferablePolicySupport;


public class TransferablePolicyImportHandler implements ImportHandler {
	
	private TransferablePolicySupport transferablePolicySupport;
	
	
	public TransferablePolicyImportHandler(TransferablePolicySupport transferablePolicySupport) {
		this.transferablePolicySupport = transferablePolicySupport;
	}
	
	private boolean canImportCache = false;
	
	
	@Override
	public boolean canImport(TransferSupport support) {
		if (support.isDrop())
			support.setShowDropLocation(false);
		
		Transferable t = support.getTransferable();
		
		try {
			canImportCache = transferablePolicySupport.getTransferablePolicy().accept(t);
		} catch (InvalidDnDOperationException e) {
			// for some reason the last transferable has no drop current
		}
		
		return canImportCache;
	}
	

	@Override
	public boolean importData(TransferSupport support) {
		boolean add = false;
		
		if (support.isDrop() && (support.getDropAction() == TransferHandler.COPY))
			add = true;
		
		Transferable t = support.getTransferable();
		
		transferablePolicySupport.getTransferablePolicy().handleTransferable(t, add);
		
		return true;
	}
	
}
