# Address_Book
Address Book is a application that allows users to save information about others
 that they wish to keep out of their usual contacts app due to whatever reason they have.
 
Users may store The first and last name of the person, their email address, their phone number, and their address.
  All contacts must have a First and Last Name. All other fields are optional.
  On the main application, on the top right will be a menu and a gear icon.
  By clicking the gear icon a user will open the shared preferences menu which currently
    only adds the option of a darkmode.
  The user may gain information about the developer by pressing the menu on the top right of the main screen and selecting the "About" option.
  Also on the main application, all contact are sorted by the last name of the contact.
  
A user may add a contact by pressing the menu on the top right of the main screen and selecting the "Add contact" option
  They will be directed to another activity with empty fields, they are only required to input a First and Last Name.

Within the Add Contact activity, they may save a contact after they have input the required information
  by either pressing the back button or by pressing the Save Contact button within the menu on the top right.
  Users may also cancel contact creation by pressing the 'Cancel' button within the menu on the top right.
  
Once a user has a contact created they May view it's details by simply pressing the item within the recycler view
  with the name of the contact they have made.
  When pressed the user will return to the activity they used to create the contact, but the fields will be uneditable.
  The user may edit a contact by pressing the 'Edit Contact' button within the menu on the top right.
  This opens all fields to be edited once more, the Contact will still require a First and Last name.
  The user may cancel while editing a contact or press the save button as before when editing a contact.
  
Within the view contact activity there are also some cool features that were added at the suggestion of Dr.Nicholson.
  While viewing a contact a user may notice the icons next to the three fields: address, email, and Phone.
  The text within these fields are also colored blue to draw a user's attention to them.
  by pressing any of these fields while viewing a contact, a user will open an application based on the pressed field.
  Address will open the map to the location of the given address if possible.
  Email will open the default email client of the phone with that address in the 'to:' field.
  Phone will open the user's messages app to send a text message to the given number.
  these fields will not open any of these applications if they are not available, for example if the user does not have a email application
  an alertdialog will open to explain the issue.
  These fields will also not open any of these applications if they are empty, again an alertDialog will open to explain that to the user.
  
