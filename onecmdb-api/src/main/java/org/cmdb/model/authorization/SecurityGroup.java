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
package org.cmdb.model.authorization;

import java.util.ArrayList;
import java.util.List;

public class SecurityGroup {
	private String name;
	private List<SecurityGroup> children = new ArrayList<SecurityGroup>();
	private SecurityGroup parent = null;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SecurityGroup parent() {
		return parent;
	}

	public void putParent(SecurityGroup parent) {
		this.parent = parent;
	}

	public void setChildren(List<SecurityGroup> children) {
		this.children = children;
		if (this.children != null) {
			for (SecurityGroup g : this.children) {
				g.putParent(this);
			}
		}
	}
	
	public List<SecurityGroup> getChildren() {
		return(this.children);
	}
	
	
	
	
	
	
}
