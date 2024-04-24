package StudentClasses;

import TestConfig.EnvironmentSetup;

public class Student_TestData {
    public Long student_Id;
    public String Student_refresh_Token;
    public Long student_not_exist;
    public Long Archived_Class;
    public Long Class_Has_No_Seats;
    public Long resource_id_with_deleted_learning_record;
    public String student_refreshToken_not_exist;
    public String student_refreshToken_deleted;
    public String Parent_refreshToken;

    public Student_TestData(){
        Environment_Variables(EnvironmentSetup.env);
    }
    public void Environment_Variables(String environment) {
        switch (environment) {
            case "BETA":
                break;
            case "DEMO":
                student_Id = 643101306816L;
                Student_refresh_Token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOnsidXNlcl9pZCI6IjY0MzEwMTMwNjgxNiIsInJvbGUiOiJzdHVkZW50In0sImV4cCI6MTcyMDA3NjE0MC43MTI2NjcsInR5cGUiOiJyZWZyZXNoIiwianRpIjoiZjNmN2U0ZGM2YjNlNGYxOGI1YmIyNDVjYWE1YTFhM2IifQ.IcCeA0zphWeG3oKeq2jDHxkWnj1PT72zjI5ZlCGe2W4";
                student_not_exist = 714129496873L;
                student_refreshToken_not_exist = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOnsidXNlcl9pZCI6IjcxNDEyOTQ5Njg3MyIsInJvbGUiOiJzdHVkZW50In0sImV4cCI6MTcyMDA3NjgyOS44OTgzOCwidHlwZSI6InJlZnJlc2giLCJqdGkiOiIzNmY2ODEzNzNlOWE0YmNkODYzMjVmNjg3N2M1NGEwNCJ9.h5-bM6kXSqHcioMlQg5AchGUrLpVgo38OidU8ab34sc";
                student_refreshToken_deleted = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOnsidXNlcl9pZCI6IjcxNDEyOTQ5Njg3MiIsInJvbGUiOiJzdHVkZW50In0sImV4cCI6MTcyMDA3Njg2Ni4zNjU3NDIsInR5cGUiOiJyZWZyZXNoIiwianRpIjoiZWEwYTBlYjViMjA0NDZmYzg4ODU3MDM4YTU2MGJmNWQifQ.8NewRVoTcK069xgJvgyYpvuMqpdxVPYmELkhxr-p9H0";
                Parent_refreshToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOnsiY3JlYXRlX2FjY291bnRfZm9yIjoiUGFyZW50X3VzZXJOYW1lQG5hZ3dhLmNvbSIsInRva2VuX3Njb3BlIjoiY3JlYXRlX2FjY291bnQifSwiZXhwIjoxNzIwMDc2OTkwLjUwOTg4NSwidHlwZSI6InJlZnJlc2giLCJqdGkiOiIxZWM3NmU3M2MzMjI0YTI4YWM5NzBlOWE1YWUzYjE2OSJ9.Ssq2d5D_Cipxcg2UAZMAHwaNJ_jdvhnBwGHrxEcKLUo";
                Archived_Class = 125193641793L;
                Class_Has_No_Seats = 838130681495l;
                resource_id_with_deleted_learning_record = 882207477853L;
                break;
            case "LIVE":
                student_Id = 870198947097L;
                Student_refresh_Token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOnsidXNlcl9pZCI6Ijg3MDE5ODk0NzA5NyIsInJvbGUiOiJzdHVkZW50In0sImV4cCI6MTcxOTgyMjg2OS43MTM1MiwidHlwZSI6InJlZnJlc2giLCJqdGkiOiI1Yzc5YmQwYmE5ODE0YmJkOTdiZGJjNDAwYWFiZGNkMSJ9.NCyk5CKgsvulzgD28WW2jo1uyupR8nNpX8wv-hSiILs";
                break;
            default:
                throw new IllegalArgumentException("Invalid environment: " + environment);
        }
    }

}
