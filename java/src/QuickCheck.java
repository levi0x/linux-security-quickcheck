
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.ZonedDateTime;

public class QuickCheck {

    static String run(String... cmd) throws Exception {
        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.redirectErrorStream(true);
        Process p = pb.start();

        StringBuilder out = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) out.append(line).append("\n");
        }
        int code = p.waitFor();
        out.append("[exit code: ").append(code).append("]\n");
        return out.toString();
    }

    public static void main(String[] args) {
        System.out.println("Security QuickCheck (Java)");
        System.out.println("Run time: " + ZonedDateTime.now());
        System.out.println("User: " + System.getProperty("user.name"));
        System.out.println("OS: " + System.getProperty("os.name") + " " + System.getProperty("os.version"));
        System.out.println("Arch: " + System.getProperty("os.arch"));
        System.out.println();

        try {
            // These commands are Linux/macOS friendly. (Windows will need different commands later.)
            System.out.println("=== whoami ===");
            System.out.println(run("whoami"));

            System.out.println("=== uname -a ===");
            System.out.println(run("uname", "-a"));

            System.out.println("=== logged-in users (who) ===");
            System.out.println(run("who"));

            System.out.println("=== listening ports (best-effort) ===");
            // macOS uses lsof nicely; Linux often has ss. We'll try both.
            try {
                System.out.println(run("lsof", "-nP", "-iTCP", "-sTCP:LISTEN"));
            } catch (Exception e) {
                System.out.println(run("sh", "-lc", "ss -lntup || netstat -tulpen"));
            }

        } catch (Exception e) {
            System.err.println("Error running checks: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

