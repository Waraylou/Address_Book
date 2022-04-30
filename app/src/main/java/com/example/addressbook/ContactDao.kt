package com.example.addressbook

import androidx.room.*

@Dao
interface ContactDao {
    @Insert
    fun insertContact(contact: Contact) : Long

    @Query("SELECT * FROM Contact ORDER BY lastName")
    fun getContactsByTitle(): List<Contact>

    @Update
    fun updateContact(contact: Contact)

    @Query("SELECT * FROM Contact WHERE contactId = :id")
    fun getContact(id: Long): Contact

    @Query("DELETE FROM Contact WHERE contactId = :id")
    fun deleteContact(id: Long)
}