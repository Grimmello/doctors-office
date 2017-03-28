import java.util.List;
import java.util.ArrayList;
import org.sql2o.*;

public class Doctor {
  private String doctorName;
  private int id;

  public Doctor(String name) {
    this.doctorName = name;
  }

  public String getDoctorName() {
    return doctorName;
  }

  public int getId() {
    return id;
  }

  public static List<Doctor> all() {
    String sql = "SELECT id, doctorName FROM doctors";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Doctor.class);
    }
  }

  public static Doctor find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM doctors WHERE id=:id";
      Doctor doctor = con.createQuery(sql)
      .addParameter("id", id)
      .executeAndFetchFirst(Doctor.class);
      return doctor;
    }
  }

  public List<Patient> getPatient() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM patients WHERE doctorId=:id";
      return con.createQuery(sql)
        .addParameter("id", this.id)
        .executeAndFetch(Patient.class);
    }
  }

  @Override
  public boolean equals(Object otherDoctor) {
    if (!(otherDoctor instanceof Doctor)) {
      return false;
    } else {
      Doctor newDoctor = (Doctor) otherDoctor;
      return this.getDoctorName().equals(newDoctor.getDoctorName()) &&
      this.getId() == newDoctor.getId();
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO doctors(doctorName) VALUES (:doctorName)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("doctorName", this.doctorName)
        .executeUpdate()
        .getKey();
    }
  }


}
