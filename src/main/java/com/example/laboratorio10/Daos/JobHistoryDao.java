package com.example.laboratorio10.Daos;

import com.example.laboratorio10.Beans.Department;
import com.example.laboratorio10.Beans.Job;
import com.example.laboratorio10.Beans.JobHistory;


import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JobHistoryDao extends DaoBase{

    public ArrayList<JobHistory> listarJobHistories(int employeeId) {

        ArrayList<JobHistory> lista = new ArrayList<>();
        String sql = "select jh.employee_id, jh.start_date, jh.end_date, j.job_title, d.department_name from job_history jh\n" +
                "inner join jobs j on jh.job_id = j.job_id\n" +
                "inner join departments d on jh.department_id = d.department_id\n" +
                "where employee_id= ?;";
        try (Connection conn = this.getConection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.setInt(1, employeeId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    JobHistory jobHistory = new JobHistory();
                    jobHistory.setEmployeeId(rs.getInt(1));
                    jobHistory.setStartDate(rs.getString(2));
                    jobHistory.setEndDate(rs.getString(3));
                    Job job = new Job();
                    job.setJobTitle(rs.getString(4));
                    jobHistory.setJob(job);
                    Department department = new Department();
                    department.setDepartmentName(rs.getString(5));
                    jobHistory.setDepartment(department);
                    lista.add(jobHistory);
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return lista;
    }

    public JobHistory obtenerUltimoJob(int idE){

        JobHistory jobHistory=null;
        String sql = "SELECT * FROM job_history WHERE employee_id = ? order by end_date desc  limit 0,1";
        try (Connection conn = this.getConection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.setInt(1, idE);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    jobHistory = new JobHistory();
                    jobHistory.setEmployeeId(rs.getInt(1));
                    jobHistory.setStartDate(rs.getString(2));
                    jobHistory.setEndDate(rs.getString(3));
                    Job job = new Job();
                    job.setJobTitle(rs.getString(4));
                    jobHistory.setJob(job);
                    Department department = new Department();
                    department.setDepartmentName(rs.getString(5));
                    jobHistory.setDepartment(department);
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return jobHistory;
    }

    public void insertarJobHistory(JobHistory job){

        String sql = "Insert into job_history(employee_id,start_date,end_date,job_id,department_id) values(?,?,?,?,?)";
        try (Connection conn = this.getConection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.setInt(1, job.getEmployeeId());
            pstmt.setString(1, job.getStartDate());
            pstmt.setString(1, job.getEndDate());
            pstmt.setString(1, job.getJob().getJobId());
            pstmt.setInt(1, job.getDepartment().getDepartmentId());
            pstmt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }


}