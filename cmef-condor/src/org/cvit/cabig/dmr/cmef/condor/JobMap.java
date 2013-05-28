/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


package org.cvit.cabig.dmr.cmef.condor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * JobMap matches the Condor-assigned "cluster id" (condorJobId) with the
 * CMEF/Anzo-assigned Job.id. ComputationJob.jobNumber == condorJobId. JobMap
 * also stores the directory in which the job is running.
 * 
 * @author Thomas
 * 
 */
public class JobMap {

	private static final String CREATE_TABLE_DDL = "CREATE TABLE IF NOT EXISTS Job "
			+ "(condorJobId VARCHAR(40), "
			+ "cmefJobId VARCHAR(40), "
			+ "cmefJobDir VARCHAR(80));";

	private String databaseFileName;

	private static final String databaseType = "org.sqlite.JDBC";
	private static final boolean isDriverLoaded = false;

	public JobMap(String databaseFilename) {

		if (!isDriverLoaded) {
			try {
				Class.forName(databaseType);
			} catch (ClassNotFoundException e) {
				System.err.println("sqlite driver not found");
				System.exit(1);
			}
		}
		this.databaseFileName = databaseFilename;
		try {
			Connection conn = getConnection();
			Statement stat;
			stat = conn.createStatement();
			stat.executeUpdate(CREATE_TABLE_DDL);
			stat.close();
			conn.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	protected Connection getConnection() {
		try {
			return DriverManager.getConnection("jdbc:sqlite:"
					+ databaseFileName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getCMEFJobId(String condorJobId) {
		String returnVal = null;

		try {
			Connection con = getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT cmefJobId FROM Job WHERE condorJobId='"
							+ condorJobId + "'");
			if (rs.next())
				returnVal = rs.getString(1);
			stmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return returnVal;
	}

	public String getCondorJobId(String cmefJobId) {
		String returnVal = null;

		try {
			Connection con = getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT condorJobId FROM Job WHERE cmefJobId='"
							+ cmefJobId + "'");
			if (rs.next())
				returnVal = rs.getString(1);
			stmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return returnVal;
	}

	public String getCMEFJobDirByCMEFJobId(String cmefJobId) {
		String returnVal = null;

		try {
			Connection con = getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT cmefJobDir FROM Job WHERE cmefJobId='"
							+ cmefJobId + "'");
			if (rs.next())
				returnVal = rs.getString(1);
			stmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return returnVal;
	}

	public String getCMEFJobDirByCondorJobId(String condorJobId) {
		String returnVal = null;

		try {
			Connection con = getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT cmefJobDir FROM Job WHERE condorJobId='"
							+ condorJobId + "'");
			if (rs.next())
				returnVal = rs.getString(1);
			stmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return returnVal;
	}

	public void put(String condorJobId, String cmefJobId, String cmefJobDir) {
		try {
			Connection con = getConnection();
			Statement stmt = con.createStatement();
			if (getCMEFJobId(condorJobId) == null) {
				stmt.executeUpdate("INSERT INTO Job VALUES ('" + condorJobId
						+ "', '" + cmefJobId + "', '" + cmefJobDir + "')");
			} else {
				stmt.executeUpdate("UPDATE Job SET condorJobId='" + condorJobId
						+ "', cmefJobDir='" + cmefJobId + "' WHERE cmefJobId='"
						+ cmefJobId + "'");
			}
			stmt.close();
			con.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void removeByCondorJobId(String condorJobId) {
		try {
			Connection con = getConnection();
			Statement statement = con.createStatement();
			statement.executeUpdate("DELETE FROM Job WHERE condorJobId= '"
					+ condorJobId + "'");
			statement.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void removeByCMEFJobId(String cmefJobId) {
		try {
			Connection con = getConnection();
			Statement statement = con.createStatement();
			statement.executeUpdate("DELETE FROM Job WHERE cmefJobId= '"
					+ cmefJobId + "'");
			statement.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int size() {
		int size = 0;
		try {
			Connection conn = getConnection();
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery("Select * from Job");
			while (rs.next())
				size++;
			rs.close();
			stat.close();
			conn.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return size;
	}

	public Collection<String> listCondorJobIds() {
		List<String> results = new ArrayList<String>();
		try {
			Connection connection = getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement
					.executeQuery("SELECT condorJobId FROM Job");
			while (resultSet.next())
				results.add(resultSet.getString(1));
			resultSet.close();
			statement.close();
			connection.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return results;
	}

	public void printAll() {
		try {
			Connection conn = getConnection();
			Statement stat = conn.createStatement();
			ResultSet rs = stat
					.executeQuery("Select cmefJobId, condorJobId, cmefJobDir from Job;");
			while (rs.next())
				System.out.println("cmefJobId=" + rs.getString(1)
						+ " : condorJobId=" + rs.getString(2)
						+ " : cmefJobDir=" + rs.getString(3));

			stat.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void empty() {
		try {
			Connection conn = getConnection();
			Statement stat = conn.createStatement();
			stat.executeUpdate("DELETE FROM Job;");

			stat.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
