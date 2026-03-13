// modeled off of Student not from repo

package com.fitzgerald.studentRegDemo.model;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int size;
    private String room;
    private String instructor;

    @ManyToMany
    private List<Student> roster = new ArrayList<>();

    // constructors
    public Course() {}
    public Course(String name, int size, String room, String instructor) {
        this.name = name;
        this.size = size;
        this.room = room;
        this.instructor = instructor;
    }

    public Long getId() { 
        return id; }

    public String getName() {
         return name; }

    public int getSize() {
         return size; }

    public String getRoom() {
         return room; }

    public String getInstructor() {
         return instructor; }

    public List<Student> getRoster() {
         return roster; }

    public void setName(String name) {
         this.name = name; }

    public void setSize(int size) {
         this.size = size; }

    public void setRoom(String room) {
         this.room = room; }

    public void setInstructor(String instructor) {
         this.instructor = instructor; }
}
