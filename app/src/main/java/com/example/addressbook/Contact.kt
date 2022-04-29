package com.example.addressbook
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
data class Contact(
    @PrimaryKey(autoGenerate = true) val contactId: Long,
    @ColumnInfo val name: String,
    @ColumnInfo val phone_number: String,
    @ColumnInfo val email: String,
    @ColumnInfo val address: String,
    @ColumnInfo val description: String,

){
    override fun toString(): String {
        return "{name = ${name} \n " +
                "phone_number = ${phone_number} \n" +
                "email = ${email} \n" +
                "address = ${address} \n" +
                "description = ${description}}"
    }
}
