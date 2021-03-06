/*
 * Lokomo OneCMDB - An Open Source Software for Configuration
 * Management of Datacenter Resources
 *
 * Copyright (C) 2006 Lokomo Systems AB
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA.
 * 
 * Lokomo Systems AB can be contacted via e-mail: info@lokomo.com or via
 * paper mail: Lokomo Systems AB, Sv�rdv�gen 27, SE-182 33
 * Danderyd, Sweden.
 *
 */
package org.cmdb.service;

import java.util.Date;

/**
 * <code>IRfcResult</code> is the response for a commit of changes.
 * 
 *
 */
public interface IRfcResult {
	/**
	 * Reflects if a commit changes was rejected or not.<br>
	 * If it was rejected a reject cause can be obtained from 
	 * getRejectCause().
	 * @return false if commit was ok, else true.
	 */
	public boolean isRejected();
	
	/**
	 * The main cause for a rejection. If not rejected null is returned.
	 * 
	 * @return a String describing the cause.
	 */
	public String getRejectCause();
	
	/**
	 * The id of the transaction that the handled the commit().
	 * @return
	 */
	public Long getTxId();
	
	/**
	 * 
	 * @return
	 */
	public String getTxIdAsString();
	
	/**
	 * 
	 */
	public Integer getCiAdded();
	
	/**
	 * 
	 */
	public Integer getCiModified();

	/**
	 * 
	 */
	public Integer getCiDeleted();
	
	/**
	 * 
	 */
	public Date getStart();
	
	/**
	 * 
	 */
	public Date getStop();
	
}
