package com.revature.Model;

//The Role model keeps track of user permissions. Can be expanded for more features later.
//Could be Admin = 0, Employee = 1, Standard = 2, or Premium = 3

public class Role {
	private int roleId; // primary key
	private String role; // not null, unique

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
//	public String getRoleFromRole() {
//		return role;
//	}
//
//	public void setRole(String role) {
//		this.role = role;
//	}
	public void setDescription(int roleid)
	{
		if (roleid == 0)
		{
			this.setRole("Admin");
		}
		else if (roleid == 1)
		{
			this.setRole("Employee");
		}
		else if (roleid == 2)
		{
			this.setRole("Standard");
		}
		else if (roleid == 3)
		{
			this.setRole("Premium");
		}
		else
		{
			this.setRole("Invalid role code passed in");
		}
	}

	
}
