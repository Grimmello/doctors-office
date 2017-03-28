import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.sql2o.*;

public class Patient {
  private String patientName;
  private int id;
  private int doctorId;
  private String patientDOB;

  public Patient(String name, int doctorId, String patientDOB) {
    this.patientName = name;
    this.doctorId = doctorId;
    this.patientDOB = patientDOB;
  }

  public String getPatientName() {
    return patientName;
  }

  public int getId() {
    return id;
  }

  public int getDoctorId() {
    return doctorId;
  }

  public static List<Patient> all() {
    String sql = "SELECT id, patientName, doctorId FROM patients";
    try(Connection con = DB.sql2o.open()) {
     return con.createQuery(sql).executeAndFetch(Patient.class);
    }
  }

  @Override
  public boolean equals(Object otherPatient){
    if (!(otherPatient instanceof Patient)) {
      return false;
    } else {
      Patient newPatient = (Patient) otherPatient;
      return this.getPatientName().equals(newPatient.getPatientName()) &&
             this.getId() == newPatient.getId() &&
             this.getDoctorId() == newPatient.getDoctorId();
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO patients(patientName, patientDOB, doctorId) VALUES (:patientName, :patientDOB, :doctorId)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("patientName", this.patientName)
        .addParameter("patientDOB", this.patientDOB)
        .addParameter("doctorId", this.doctorId)
        .executeUpdate()
        .getKey();
    }
  }

  public static Patient find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM patients where id=:id";
      Patient patient = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Patient.class);
      return patient;
    }
  }

  public void update(String description) {
    try(Connection con = DB.sql2o.open()) {
    String sql = "UPDATE patients SET patientName = :patientName WHERE id = :id";
    con.createQuery(sql)
      .addParameter("patientName", patientName)
      .addParameter("id", id)
      .executeUpdate();
    }
  }

}
