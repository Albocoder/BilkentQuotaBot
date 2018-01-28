# BilkentQuotaBot
This is a Java based quota bot for bilkent offerings bypassing the captcha!
It is just a product of my offerings scrapper which will also be updated upon this!

This is getting updated to handle the meaningless captcha block by BCC also uses JSoup because it rocks!

# How to use
0. DOWNLOAD AND EXTRACT THE READY-TO-USE BOT <a href="https://github.com/Albocoder/BilkentQuotaBot/releases/download/1.0.1/quotabot-1.0.1.zip">HERE</a>.(recommended)
1. You must have java installed. Go to your cmd and type "java" to verify if it works. Install it <a href="https://java.com/en/download/help/windows_manual_download.xml">here</a>(chances are you are on a Windows machine) if you get a message like "command doesnt exist".
2. Use your favorite browser to manually access offerings page (manually write the chaptcha and get in like you usually do)
3. Open "Inspect element" and go to "Storage" or "Cookie" tab
4. Find a cookie named <i>PHPSESSID</i>(it must be there just look for it)
5. Copy the value of that cookie and replace it with the one from the demo <b>config.xml</b> file in the "cookie" field.
6. Inside the "targets" tag add your targets separately as "target" tag (you may delete the 3 prewritten demo targets).
7. The department, course code and section must be provided
8. Once you get your targets in the xml save the file in the same place as the jar.
9. Open terminal from same place as where you have the jar and xml and run the program with the following command <i>java -jar quotabot.jar</i>
10. Happy botting! And drop me a star if you like it :)

### Demo video: https://www.youtube.com/watch?v=zoNKR1WA3e8

# Disclaimer
This is a side project I came up with. It's intended for educational purposes only. Use it at your own risk. I'm not held responsible by any use of this software.

#### Hack to learn don't learn to hack!
