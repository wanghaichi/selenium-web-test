import java.io.*;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import com.csvreader.*;

public class Main {

    public static void main(String[] args){


        //定义测试的根目录
        String baseUrl = "http://121.193.130.195:8080";
        // 创建webdriver对象
        System.setProperty("webdriver.gecko.driver", "geckodriver");
        WebDriver driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        try{
            //读取csv文件
            File inFile = new File("inputgit.csv");
            BufferedReader reader = new BufferedReader(new FileReader(inFile));
            CsvReader csvreader = new CsvReader(reader,',');
            //每次读取一行
            while(csvreader.readRecord()){
                String str = csvreader.getRawRecord();
                String[] strList = str.split(",");

                //使用webdriver模拟登录，并获取信息
                driver.get(baseUrl + "/");
                driver.findElement(By.id("name")).clear();
                driver.findElement(By.id("name")).sendKeys(strList[0]);
                driver.findElement(By.id("pwd")).clear();
                driver.findElement(By.id("pwd")).sendKeys(strList[0].substring(4));
                driver.findElement(By.id("submit")).click();
                String temp = strList[0];
                strList[0] = strList[1];
                strList[1] = temp;
                //获取特定验证信息
                String[] targetGitUrl = driver.findElement(By.id("resultString")).getAttribute("innerHTML").trim().split(",");
                boolean flag = true;
                for(int i = 0; i < 3; i ++){
                    if(!strList[i].equals(targetGitUrl[i])){
                        flag = false;
                        break;
                    }
                }
                //验证失败
                if(!flag){
                    System.out.println("Error: \n" + strList[0] + " " + strList[1] + " " + strList[2]);
                    System.out.println(targetGitUrl[0] + " " + targetGitUrl[1] + " " + targetGitUrl[2]);
                }
            }
            driver.quit();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
