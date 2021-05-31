import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.MobileCapabilityType;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static io.appium.java_client.touch.WaitOptions.waitOptions;
import static io.appium.java_client.touch.offset.PointOption.point;
import static java.time.Duration.ofMillis;

public class Automation {
    AndroidDriver<AndroidElement> androidDriver;

    public Automation() throws MalformedURLException {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "9cb5c0c");
        desiredCapabilities.setCapability("platformName", "android");
        desiredCapabilities.setCapability("appPackage", "com.instagram.android");
        desiredCapabilities.setCapability("appActivity", ".activity.MainTabActivity");
        desiredCapabilities.setCapability("noReset", true);
        androidDriver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), desiredCapabilities);
    }

    private void follow_last_searched_account(int followers_max, int waiting) throws InterruptedException {
        MobileElement search_btn = androidDriver.findElementByXPath("//android.widget.Button[@content-desc='Search and Explore']");
        search_btn.click();
        Thread.sleep(3000);

        androidDriver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/" +
                "android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/" +
                "android.view.ViewGroup/android.widget.FrameLayout[2]/android.widget.FrameLayout[2]/android.widget.FrameLayout/" +
                "android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.EditText").click();
        Thread.sleep(3000);

        androidDriver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/" +
                "android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/" +
                "android.view.ViewGroup/android.widget.FrameLayout[2]/android.widget.FrameLayout[1]/android.widget.FrameLayout/" +
                "androidx.viewpager.widget.ViewPager/android.widget.FrameLayout/androidx.recyclerview.widget.RecyclerView/android.widget.FrameLayout[1]/" +
                "android.widget.LinearLayout/android.widget.LinearLayout").click();
        Thread.sleep(3000);

        MobileElement followers = androidDriver.findElementById("com.instagram.android:id/row_profile_header_followers_container");
        followers.click();
        Thread.sleep(3000);

        //loop 1->10 for the first time 2->10 for the loops
        int j = 2;
        int followers_nb = 0;

        while (followers_nb < followers_max) {
            MobileElement element = null;
            for (; j < 10; j++) {
                Random random = new Random();
                element = androidDriver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/" +
                        "android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/" +
                        "android.view.ViewGroup/android.widget.FrameLayout[2]/android.widget.FrameLayout[1]/android.widget.LinearLayout/" +
                        "android.widget.FrameLayout/androidx.viewpager.widget.ViewPager/android.widget.FrameLayout/android.widget.ListView/" +
                        "android.widget.LinearLayout[" + j + "]/android.widget.LinearLayout");
                try {
                    var btn_follow = element.findElementById("com.instagram.android:id/button");
                    if (btn_follow.getText().equals("Follow")) {
                        btn_follow.click();
                        followers_nb++;
                        System.out.println("following => " + followers_nb);
                        Thread.sleep(random.nextInt(waiting));
                    }
                } catch (Exception ignored) {

                }
            }
            j = 1;
            var elementFirst = androidDriver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/" +
                    "android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/" +
                    "android.view.ViewGroup/android.widget.FrameLayout[2]/android.widget.FrameLayout[1]/android.widget.LinearLayout/" +
                    "android.widget.FrameLayout/androidx.viewpager.widget.ViewPager/android.widget.FrameLayout/android.widget.ListView/" +
                    "android.widget.LinearLayout[1]");
            var elementLast = androidDriver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/" +
                    "android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/" +
                    "android.view.ViewGroup/android.widget.FrameLayout[2]/android.widget.FrameLayout[1]/android.widget.LinearLayout/" +
                    "android.widget.FrameLayout/androidx.viewpager.widget.ViewPager/android.widget.FrameLayout/android.widget.ListView/" +
                    "android.widget.LinearLayout[10]");
            new TouchAction(androidDriver)
                    .press(point(elementLast.getCenter().x, elementLast.getCenter().y))
                    .moveTo(point(elementFirst.getCenter().x, elementFirst.getCenter().y))
                    .release().perform();
        }
        System.out.println(followers_max + " reached");
    }

    private void like_home_posts(int likes_max, int waiting) throws InterruptedException {

        //loop 1->10 for the first time 2->10 for the loops
        int links_nb = 0;

        while (links_nb < likes_max) {
            Random random = new Random();
            try {
                MobileElement like_btn = androidDriver.findElementsByXPath("//android.widget.ImageView[@content-desc='Like']").get(0);
                like_btn.click();
                links_nb++;
                System.out.println("likes => " + links_nb);
                Thread.sleep(random.nextInt(waiting));
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            } finally {
                new TouchAction(androidDriver)
                        .press(point(500, 1500))
                        .waitAction(waitOptions(ofMillis(500)))
                        .moveTo(point(555, 555)).release().perform();
                Thread.sleep(random.nextInt(1000));
            }

            try {
                MobileElement new_posts = androidDriver.findElementByXPath("");
                new_posts.click();
            } catch (Exception ignore) {
            }
        }
        System.out.println(likes_max + " reached");
    }

    private void unFollow(List<String> follwers, int unfollow_max,int waiting) throws InterruptedException {
        MobileElement search_btn = androidDriver.findElementByXPath("//android.widget.Button[@content-desc='Profile']/android.view.ViewGroup");
        search_btn.click();
        Thread.sleep(5000);
        MobileElement flws = androidDriver.findElementById("row_profile_header_following_container");
        flws.click();
        Thread.sleep(5000);
        int unfollow_nbr = 0;
        while (unfollow_nbr < unfollow_max) {
            MobileElement element = null;
            for (int i = 0; i < 10; i++) {
                Random random = new Random();
                try {
                    element = androidDriver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/" +
                            "android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/" +
                            "android.view.ViewGroup/android.widget.FrameLayout[2]/android.widget.FrameLayout[1]/android.widget.LinearLayout/" +
                            "android.widget.FrameLayout/androidx.viewpager.widget.ViewPager/android.widget.FrameLayout/android.widget.ListView/" +
                            "android.widget.LinearLayout[" + i + "]/android.widget.LinearLayout");
                    var btn_unFollow = element.findElementById("com.instagram.android:id/button");
                    var userName = element.findElementById("follow_list_username").getText();
                    if (btn_unFollow.getText().equals("Following") && !follwers.contains(userName)) {
                        btn_unFollow.click();
                        unfollow_nbr++;
                        System.out.println("unfollowed => " + unfollow_nbr);
                        try {
                            androidDriver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/" +
                                    "android.widget.FrameLayout/android.view.ViewGroup/android.widget.LinearLayout/android.widget.FrameLayout[1]/" +
                                    "android.widget.Button").click();

                        } catch (Exception ignore) {
                        }
                        Thread.sleep(random.nextInt(waiting));
                    }
                } catch (Exception ignored) {
                }
            }

            var elementFirst = androidDriver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/" +
                    "android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/" +
                    "android.view.ViewGroup/android.widget.FrameLayout[2]/android.widget.FrameLayout[1]/android.widget.LinearLayout/" +
                    "android.widget.FrameLayout/androidx.viewpager.widget.ViewPager/android.widget.FrameLayout/android.widget.ListView/" +
                    "android.widget.LinearLayout[3]");
            var elementLast = androidDriver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/" +
                    "android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/" +
                    "android.view.ViewGroup/android.widget.FrameLayout[2]/android.widget.FrameLayout[1]/android.widget.LinearLayout/" +
                    "android.widget.FrameLayout/androidx.viewpager.widget.ViewPager/android.widget.FrameLayout/android.widget.ListView/" +
                    "android.widget.LinearLayout[6]");
            new TouchAction(androidDriver)
                    .press(point(elementLast.getCenter().x, elementLast.getCenter().y))
                    .waitAction(waitOptions(ofMillis(250)))
                    .moveTo(point(elementFirst.getCenter().x, elementFirst.getCenter().y))
                    .release().perform();
        }
    }

    private List<String> getFollowers() throws InterruptedException {
        Random random = new Random();
        androidDriver.findElementByXPath("//android.widget.Button[@content-desc='Profile']/android.view.ViewGroup").click();
        Thread.sleep(random.nextInt(5000));
        androidDriver.findElementById("com.instagram.android:id/row_profile_header_followers_container").click();
        Thread.sleep(random.nextInt(5000));
        var followers = new ArrayList<String>();
        var followers_max = 10;
        var followers_nb = 0;
        while (followers_nb < followers_max) {
            for (int i = 0; i < 8; i++) {
                String follower = "blank";
                try {
                    follower = androidDriver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout" +
                            "/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.view.ViewGroup/" +
                            "android.widget.FrameLayout[2]/android.widget.FrameLayout[1]/android.widget.LinearLayout/android.widget.FrameLayout/" +
                            "androidx.viewpager.widget.ViewPager/android.widget.FrameLayout/android.widget.ListView/android.widget.LinearLayout[" + i + "]/" +
                            "android.widget.LinearLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.TextView").getText();
                } catch (Exception ignore) {
                }
                try {
                    follower = androidDriver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout" +
                            "/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.view.ViewGroup/" +
                            "android.widget.FrameLayout[2]/android.widget.FrameLayout[1]/android.widget.LinearLayout/android.widget.FrameLayout/" +
                            "androidx.viewpager.widget.ViewPager/android.widget.FrameLayout/android.widget.ListView/android.widget.LinearLayout[" + i + "]/" +
                            "android.widget.LinearLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.TextView[1]").getText();
                } catch (Exception ignore) {
                }

                followers_nb++;
                followers.add(follower);
                System.out.println(followers_nb);
            }
            new TouchAction(androidDriver)
                    .press(point(500, 1500))
                    .waitAction(waitOptions(ofMillis(500)))
                    .moveTo(point(555, 555)).release().perform();
            Thread.sleep(random.nextInt(1000));
        }
        System.out.println(followers.toString());
        return followers;
    }

    private List<String> extract() throws IOException {
        var followers = new ArrayList<String>();
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("followers.html").getFile());
        String data = FileUtils.readFileToString(file, "UTF-8");

        Document doc = Jsoup.parse(data);
        var nodes = doc.select("li div div span a");
        for (var node : nodes) {
            followers.add(node.text());
        }
        return followers;
    }
    //Data Warehouse Concepts, Design, and Data Integration
    private void createNewFile() {
        try {
            File myObj = new File("filename.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        var automation = new Automation();
        automation.like_home_posts(5, 30000);
        automation.follow_last_searched_account(486, 1000);
        //fileService.save(automation.extract());

        //var followers = FileService.getFollowers();
        //automation.unFollow(followers, 263,5000);
    }
}
