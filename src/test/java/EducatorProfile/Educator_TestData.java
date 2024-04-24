package EducatorProfile;

import TestConfig.EnvironmentSetup;
import TestConfig.TestBase;

public class Educator_TestData {
    public String educator_id = "758047270477";
    public String deleted_educator = "206130172222";
    public String notActive_educator = "799188532061";
    public String Admin_Token;
    public String refresh_token_for_deletedEducator;
    public String refresh_token_for_notActiveEducator;
    public String refresh_token;

    public Educator_TestData(){
        Environment_Variables(EnvironmentSetup.env);
    }
    public void Environment_Variables(String environment) {
        switch (environment) {
            case "BETA":
                Admin_Token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOnsidG9rZW5fc2NvcGUiOiJhZG1pbl9hcmVhIiwicm9sZSI6ImFkbWluIn0sImV4cCI6MTcxNjQ2MzQxOC41MzcxNzUsInR5cGUiOiJyZWZyZXNoIiwianRpIjoiYThmZmViYTk5M2NiNDFhYjgzY2ExNGQ4ZGUxNmE2Y2YifQ.kB14Ij4UcKH-xevvB93r8lC98ntpqx8Uief_SZf0XxY";
                refresh_token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOnsidXNlcl9pZCI6IjM0MzI1Njc4NjU0MyIsInJvbGUiOiJlZHVjYXRvciJ9LCJleHAiOjE3MTYzNzk5MjcuNDk2NzU2LCJ0eXBlIjoicmVmcmVzaCIsImp0aSI6IjYxNzcwODJjOTkyNDQzNWU4NmFlYTg3NmVlN2Q4NDk1In0.h2VC8kbzOkhhtNIhFoeCDxrHWG75pQZgMy3V-ThGTLU";
                refresh_token_for_deletedEducator ="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOnsidXNlcl9pZCI6IjIwNjEzMDE3MjIyMiIsInJvbGUiOiJlZHVjYXRvciJ9LCJleHAiOjE3MTU5Mzc2MTQuNDM0MzIsInR5cGUiOiJyZWZyZXNoIiwianRpIjoiZTQ3YjljOWFkMTgxNDAxYTlkOTgzY2YzNzk3NjlmNmYifQ.PnRZ5Olu8SYUxXbL3JclAQRG5NN9F2g51xhrwFEuQRI";
                refresh_token_for_notActiveEducator ="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOnsidXNlcl9pZCI6IjIwNjEzMDE3MjExMSIsInJvbGUiOiJlZHVjYXRvciJ9LCJleHAiOjE3MTU5Mzc2NDQuNTYzNTM4LCJ0eXBlIjoicmVmcmVzaCIsImp0aSI6ImVlYzQ1M2QyNDNjZjQ1ZjRiM2Y0ZWQ4NTNjZWMyMDIwIn0.IxWDDLYzVnfG6KoF-0_vNVMS5uc-ZQtWqJRoUYa-Fro";
                break;
            case "DEMO":
                Admin_Token ="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOnsidG9rZW5fc2NvcGUiOiJhZG1pbl9hcmVhIiwicm9sZSI6ImFkbWluIn0sImV4cCI6MTcxODc4OTc4My44NjE5NTEsInR5cGUiOiJyZWZyZXNoIiwianRpIjoiMDMxMjNjODk3NmU0NDRjMGFkYzg1MTNlYjg0ZGMyYWUifQ.IAEn3SiETpEOoHJHl-CGzZ758GWaZ9E13Z7EabNy8Yg";
                refresh_token ="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOnsidXNlcl9pZCI6Ijc1ODA0NzI3MDQ3NyIsInJvbGUiOiJlZHVjYXRvciJ9LCJleHAiOjE3MTg3OTc3NTUuNjIzMTU2LCJ0eXBlIjoicmVmcmVzaCIsImp0aSI6IjEyMjc4ZDVmMDlkODRjM2Q5ZjY4ZWIzZTNmYzZhODAzIn0.LSrRJ-ksSeAgLxzxzXgavFCj9qYFi7f4W6q4ZebgQHs";
                refresh_token_for_deletedEducator ="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOnsidXNlcl9pZCI6IjIwNjEzMDE3MjIyMiIsInJvbGUiOiJlZHVjYXRvciJ9LCJleHAiOjE3MTg3OTY4MzkuMDE2MzczLCJ0eXBlIjoicmVmcmVzaCIsImp0aSI6ImQ1Yzc5ZWEzZWM4MTRkN2I4ZjFkNGZiOThiNTQ4YTljIn0.iKQ3bB_cFYNuECc3R0oMnUN6FyxDtOeGHZ9PCAM5ttw";
                refresh_token_for_notActiveEducator = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOnsidXNlcl9pZCI6Ijc5OTE4ODUzMjA2MSIsInJvbGUiOiJlZHVjYXRvciJ9LCJleHAiOjE3MjAwODAwMjcuNzg2NjUsInR5cGUiOiJyZWZyZXNoIiwianRpIjoiMTVjMWQ0ZjdiYzJlNDY5YWI4NmU2N2JjNmJiMTNhNTEifQ.v4f683rFHSrNz6UvG465kGWQS8MHjwiir-9563ajL4w";
                break;
            case "LIVE":
                Admin_Token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOnsidG9rZW5fc2NvcGUiOiJhZG1pbl9hcmVhIiwicm9sZSI6ImFkbWluIn0sImV4cCI6MTcxODg5MDg0Mi4yMTIzMzQsInR5cGUiOiJyZWZyZXNoIiwianRpIjoiYWZjNmVlOTExYTlmNDllMmJiZjRiYjY3OWJmYjk2YzYifQ.-gsJg5F4ot13yNMlRTyGJ0y7KOT3H6wspK4KnNjhDDk";
                refresh_token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOnsidXNlcl9pZCI6Ijc1ODA0NzI3MDQ3NyIsInJvbGUiOiJlZHVjYXRvciJ9LCJleHAiOjE3MTg5NTQxODMuNTU0NTIzLCJ0eXBlIjoicmVmcmVzaCIsImp0aSI6ImNlNGE5M2JmMDg1OTRmM2E4MDFjM2EwMGE4ZjAxNGI0In0.biE1JHzBzH2Ezhxs9pjM6zn4g8OyhUoJTkdk4SQEbcM";
                refresh_token_for_deletedEducator = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOnsidXNlcl9pZCI6IjIwNjEzMDE3MjIyMiIsInJvbGUiOiJlZHVjYXRvciJ9LCJleHAiOjE3MTkyMTUwMjkuMDM3Nzg3LCJ0eXBlIjoicmVmcmVzaCIsImp0aSI6ImExOTk4YWM1MTE0OTRkMDg5Mzk0Y2FjMjYxZWI4MDU0In0.4hBbB5lOVE3SIwW2Z-IHRmgG2WNBnX6n2AD3-tz2KYc";
                refresh_token_for_notActiveEducator = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOnsidXNlcl9pZCI6Ijc5OTE4ODUzMjA2MSIsInJvbGUiOiJlZHVjYXRvciJ9LCJleHAiOjE3MTkzMDI2OTcuMTA5ODY4LCJ0eXBlIjoicmVmcmVzaCIsImp0aSI6IjQyMDgwM2JkNzNmOTQ3MWE5YWVlOGY0MWE5OGZiMzNiIn0.5PWqowkbRaklg9WecMRvi_4MdC1a2HZ7gUspKB4DuG0";
                break;
            default:
                throw new IllegalArgumentException("Invalid environment: " + environment);
        }
    }


}

