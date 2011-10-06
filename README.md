About
========================
I needed an app that simply runs some USSD codes that my network provides. I searched for something similar but i didn't find, so, i decided to do one. I also thought that would exist more people with the same needs. thus, i tried to put this app the more simplest and generic as possible. I didn't have a chance to test it with others network operators so if you're interested, feel free to use and collaborate. I will be glad if you notice any problems or code improvements.
The layout is also very simple, a list of all the codes mentioned by you, and a dialog showing the description of the ussd code.

[Screenshot](USSDRunner/raw/master/ss/activity.png)


Using
========================

As i said, i tried to put the code the more generic i could. The aim is that you just need to fill the *ussd_info.xml* file with your network information and that's it. However, i'm sure there will be more differences between the ussd codes of each country. If so, probably you will need to include a few lines in `onActivityResult` method (i marked the right place to code).

Now, i will describe more detailed the *ussd_info.xml* file. It has a set of JSON elements, each of them with 3 fields: *title*, title of code; *description*, what does this code actualy do; and finally the *ussd code*. Sometimes ussd codes have to deal also with contacts. If that's the case, in the field *ussd code*, in the place of contact, you need to write "contact". This will make launch a contact picker in your application, when using this ussd code.

Here is an example of JSON elements:


	<item>		
		{\"title\":\"Balance\",
		\"description\":\"The description of this ussd code.\",
		\"code\":\"*#100#\"
		}
	</item>
	
	<item>
		{\"title\":\"Validity\",
		\"description\":\"The description of this ussd code.\",
		\"code\":\"*#101#\"
		}
	</item>
	
	<item>
		{\"title\":\"Poke\",
		\"description\":\"The description of this ussd code.\",
		\"code\":\"*#112*contact#\"
		}
	</item>


You also need to fill the *prefix* of your country. `<string name="prefix">+351</string>`


Android Support
========================
Android 2.1 (tested) or above.
