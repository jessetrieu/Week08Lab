package servlets;

import businesslogic.NoteService;
import domainmodel.Note;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NoteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        NoteService us = new NoteService();
        String action = request.getParameter("action");
        if (action != null && action.equals("view")) {
            String selectedNoteId = request.getParameter("selectedNoteId");
            try {
                Note note = us.get(Integer.parseInt(selectedNoteId));
                request.setAttribute("selectedNote", note);
            } catch (Exception ex) {
                Logger.getLogger(NoteServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        List<Note> notes = null;        
        try {
            notes = us.getAll();
        } catch (Exception ex) {
            Logger.getLogger(NoteServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        request.setAttribute("notes", notes);
        getServletContext().getRequestDispatcher("/WEB-INF/notes.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String noteId = request.getParameter("noteId");
        String contents = request.getParameter("contents");
        String action = request.getParameter("action");
        
//when u insert u need to set a noteId value cuz the noteId auto increments in the database
        if (noteId== null) {
            noteId = "0";
        }
        NoteService us = new NoteService();

        try {
            if (action.equals("delete")) {
                String selectedNoteId = request.getParameter("selectedNoteId");
                us.delete(Integer.parseInt(selectedNoteId));
            } else if (action.equals("edit")) {
                Note note = new Note(Integer.parseInt(noteId), contents);
                
                us.update(note.getNoteId(), note.getContents());
            } else if (action.equals("add")) {
                Note note = new Note(Integer.parseInt(noteId), contents);
                us.insert(note.getNoteId(), note.getContents());
            }
        } catch (Exception ex) {
            request.setAttribute("errorMessage", "Whoops.  Could not perform that action.");
        }
        
        List<Note> notes = null;
        try {
            notes = us.getAll();
        } catch (Exception ex) {
            Logger.getLogger(NoteServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        request.setAttribute("notes", notes);
        getServletContext().getRequestDispatcher("/WEB-INF/notes.jsp").forward(request, response);
    }
}
