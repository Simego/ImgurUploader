ImgurUploader
=============

A simple software to make fast uploads to Imgur and have a little track of it (or not), making use of a shortcut to take screenshots that automatically uploads and give you the link to imgur.com.

Download link of the build at end of readme.

Preview:<br/>
![My image](http://i.imgur.com/4xxH4iK.png)



How to use (easy and fast!)
============
1. Create an account at Imgur.com
       (https://imgur.com/register)

2. Read the Imgur.com API page. (if you want to, i recommend)

3. Register a new application. (https://api.imgur.com/oauth2/addclient)

4. Fill all fields and select the option: "Anonymous usage without user authorization"

5. Get your Client ID key so you can use their API.

6. Launch the program and use your Client ID there. (it will be saved)

7. Click the Start button and you're good to go!

Information:<br/>
Screenshot shortcut: CTRL + SHIFT + C.<br/>
Double-click on link to open it.<br/>
Double-click on description to edit it.<br/>
Click on any row of the table and press 'DELETE' to delete the entry.


Features
============
Fullscreen screenshot<br/>
Active window screenshot<br/>
Imgur.com picture upload<br/>
Local record of all pictures sent to Imgur (sqlite)<br/>
All pictures can have descriptions.


Downloads
============
Windows x86: https://dl.dropboxusercontent.com/u/32101688/ImgurUploader%20x86.rar

Windows x64: https://dl.dropboxusercontent.com/u/32101688/ImgurUploader%20x64.rar


Special thanks
===========
JIntellitype, for the worst part: https://code.google.com/p/jintellitype/

SQLite, for the little database part: https://bitbucket.org/xerial/sqlite-jdbc (almost not needed but maybe for future versions ^^)

Apache for the Apache Commons stuff: http://commons.apache.org/

FlexJson for the json-object serializing part: http://flexjson.sourceforge.net/
