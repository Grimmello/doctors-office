import java.util.Map;
import java.util.HashMap;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;

public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/index.vtl");
      model.put("doctors", Doctor.all());
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/doctors/:doctor_id/patients/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Doctor doctor = Doctor.find(Integer.parseInt(request.params(":doctor_id")));
      Patient patient = Patient.find(Integer.parseInt(request.params(":id")));
      model.put("doctor", doctor);
      model.put("patient", patient);
      model.put("template", "templates/patient.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/doctors/:doctor_id/patients/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Patient patient = Patient.find(Integer.parseInt(request.params("id")));
      String patientName = request.queryParams("patientName");
      Doctor doctor = Doctor.find(patient.getDoctorId());
      patient.update(patientName);
      String url = String.format("/doctors/%d/patients/%d", doctor.getId(), patient.getId());
      response.redirect(url);
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/patients", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Doctor doctor = Doctor.find(Integer.parseInt(request.queryParams("doctorId")));
      String patientName = request.queryParams("patientName");
      String patientDOB = request.queryParams("patientDOB");
      Patient newPatient = new Patient(patientName, doctor.getId(), patientDOB);
      newPatient.save();
      model.put("doctor", doctor);
      model.put("template", "templates/doctor-patient-success.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/patients/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Patient patient = Patient.find(Integer.parseInt(request.params(":id")));
      model.put("patient", patient);
      model.put("template", "templates/patient.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/doctors", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String doctorName = request.queryParams("doctorName");
      Doctor newDoctor = new Doctor(doctorName);
      newDoctor.save();
      model.put("template", "templates/doctor-success.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/doctors", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("doctors", Doctor.all());
      model.put("template", "templates/doctors.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/doctors/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Doctor doctor = Doctor.find(Integer.parseInt(request.params(":id")));
      model.put("doctor", doctor);
      model.put("template", "templates/doctor.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

  }
}
