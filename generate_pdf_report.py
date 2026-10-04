import os
import sys
from reportlab.lib.pagesizes import letter
from reportlab.lib import colors
from reportlab.platypus import (
    SimpleDocTemplate, Paragraph, Spacer, Table, TableStyle, PageBreak, KeepTogether, HRFlowable, Preformatted
)
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.pdfgen import canvas

class NumberedCanvas(canvas.Canvas):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self._saved_page_states = []

    def showPage(self):
        self._saved_page_states.append(dict(self.__dict__))
        self._startPage()

    def save(self):
        num_pages = len(self._saved_page_states)
        for state in self._saved_page_states:
            self.__dict__.update(state)
            self.draw_page_decorations(num_pages)
            super().showPage()
        super().save()

    def draw_page_decorations(self, page_count):
        if self._pageNumber == 1:
            return  # Skip cover page
        
        self.saveState()
        self.setFont("Helvetica-Bold", 8)
        self.setFillColor(colors.HexColor("#2C3E50"))
        
        # Header
        self.drawString(54, 750, "RideLink Microservices Final Project Report")
        self.drawRightString(612 - 54, 750, "GitHub: https://github.com/IT24104304/RideLink.git")
        self.setStrokeColor(colors.HexColor("#BDC3C7"))
        self.setLineWidth(0.5)
        self.line(54, 742, 612 - 54, 742)
        
        # Footer
        self.line(54, 45, 612 - 54, 45)
        self.setFont("Helvetica", 8)
        self.setFillColor(colors.HexColor("#7F8C8D"))
        self.drawString(54, 32, "RideLink Distributed Architecture - Confidential")
        self.drawRightString(612 - 54, 32, f"Page {self._pageNumber} of {page_count}")
        self.restoreState()

def build_pdf():
    pdf_path = r"c:\Users\Asus\Desktop\RideLinkNelundi\RideLink\AD_Final_Report_GROUPID.pdf"
    doc = SimpleDocTemplate(
        pdf_path,
        pagesize=letter,
        leftMargin=54,
        rightMargin=54,
        topMargin=54,
        bottomMargin=54
    )

    styles = getSampleStyleSheet()

    # Custom styles
    title_style = ParagraphStyle(
        'CoverTitle',
        parent=styles['Normal'],
        fontName='Helvetica-Bold',
        fontSize=24,
        leading=28,
        textColor=colors.HexColor("#1A365D"),
        alignment=1, # Center
        spaceAfter=15
    )
    
    subtitle_style = ParagraphStyle(
        'CoverSubtitle',
        parent=styles['Normal'],
        fontName='Helvetica',
        fontSize=13,
        leading=16,
        textColor=colors.HexColor("#2B6CB0"),
        alignment=1,
        spaceAfter=25
    )

    h1_style = ParagraphStyle(
        'Heading1_Custom',
        parent=styles['Normal'],
        fontName='Helvetica-Bold',
        fontSize=16,
        leading=20,
        textColor=colors.HexColor("#1A365D"),
        spaceBefore=18,
        spaceAfter=10,
        keepWithNext=True
    )

    h2_style = ParagraphStyle(
        'Heading2_Custom',
        parent=styles['Normal'],
        fontName='Helvetica-Bold',
        fontSize=12,
        leading=15,
        textColor=colors.HexColor("#2C5282"),
        spaceBefore=12,
        spaceAfter=6,
        keepWithNext=True
    )

    body_style = ParagraphStyle(
        'Body_Custom',
        parent=styles['Normal'],
        fontName='Helvetica',
        fontSize=9.5,
        leading=13,
        textColor=colors.HexColor("#2D3748"),
        spaceAfter=8
    )

    code_style = ParagraphStyle(
        'Code_Style',
        parent=styles['Normal'],
        fontName='Courier',
        fontSize=7.5,
        leading=9.5,
        textColor=colors.HexColor("#1A202C")
    )

    link_style = ParagraphStyle(
        'Link_Style',
        parent=styles['Normal'],
        fontName='Helvetica-Bold',
        fontSize=10,
        leading=13,
        textColor=colors.HexColor("#3182CE"),
        alignment=1
    )

    story = []

    # --- COVER PAGE ---
    story.append(Spacer(1, 40))
    story.append(Paragraph("RIDELINK DISTRIBUTED MICROSERVICES SYSTEM", title_style))
    story.append(Paragraph("Application Development (AD) Final Group Project Report", subtitle_style))
    story.append(HRFlowable(width="100%", thickness=2, color=colors.HexColor("#3182CE"), spaceBefore=10, spaceAfter=20))
    
    story.append(Paragraph("<b>GitHub Repository URL:</b>", ParagraphStyle('CenterBold', parent=body_style, alignment=1)))
    story.append(Paragraph('<a href="https://github.com/IT24104304/RideLink.git"><font color="#3182CE"><u>https://github.com/IT24104304/RideLink.git</u></font></a>', link_style))
    story.append(Spacer(1, 25))

    # Group Members Table
    member_data = [
        [Paragraph("<b>Group Role</b>", body_style), Paragraph("<b>Member Name</b>", body_style), Paragraph("<b>Student ID</b>", body_style), Paragraph("<b>Primary Service Module</b>", body_style)],
        [Paragraph("<b>Group Leader</b>", body_style), Paragraph("Kulasuriya W.N.N", body_style), Paragraph("IT24104304", body_style), Paragraph("Driver & Vehicle Service", body_style)],
        [Paragraph("Member 2", body_style), Paragraph("Perera H.G.K.D", body_style), Paragraph("IT24104143", body_style), Paragraph("Ride Management Service", body_style)],
        [Paragraph("Member 3", body_style), Paragraph("Bandara J.K.T.I", body_style), Paragraph("IT24104276", body_style), Paragraph("Account Service", body_style)],
        [Paragraph("Member 4", body_style), Paragraph("Karunarathna H.W.D.S.S", body_style), Paragraph("IT24104353", body_style), Paragraph("Fare & Payment Service", body_style)],
    ]

    t = Table(member_data, colWidths=[90, 130, 90, 190])
    t.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,0), colors.HexColor("#E2E8F0")),
        ('TEXTCOLOR', (0,0), (-1,0), colors.HexColor("#1A202C")),
        ('GRID', (0,0), (-1,-1), 0.5, colors.HexColor("#CBD5E0")),
        ('VALIGN', (0,0), (-1,-1), 'MIDDLE'),
        ('PADDING', (0,0), (-1,-1), 6),
    ]))
    story.append(t)
    story.append(Spacer(1, 30))

    story.append(Paragraph("<b>Executive Summary:</b>", h2_style))
    exec_summary = ("RideLink is a comprehensive, production-grade distributed microservices application designed "
                    "for urban transportation management. Built using Spring Boot, Java 21, and MongoDB, the system "
                    "comprises four decoupled microservices: Account Service, Driver & Vehicle Service, Ride Management Service, "
                    "and Fare & Payment Service. This report outlines the system architecture, REST API design, domain models, "
                    "and complete source code for all components.")
    story.append(Paragraph(exec_summary, body_style))
    story.append(PageBreak())

    # --- SECTION 1: SYSTEM ARCHITECTURE ---
    story.append(Paragraph("1. System Architecture & Component Design", h1_style))
    story.append(HRFlowable(width="100%", thickness=1, color=colors.HexColor("#CBD5E0"), spaceBefore=4, spaceAfter=15))
    
    arch_desc = ("RideLink adopts a modern microservice-oriented architecture. Each service operates independently with "
                 "its own dedicated data repository (MongoDB) ensuring low coupling and high reliability. Communication occurs "
                 "via standardized RESTful APIs returning structured JSON object representations.")
    story.append(Paragraph(arch_desc, body_style))

    arch_table_data = [
        [Paragraph("<b>Service Module</b>", body_style), Paragraph("<b>Port</b>", body_style), Paragraph("<b>Database Name</b>", body_style), Paragraph("<b>Primary Responsibilities</b>", body_style)],
        [Paragraph("Account Service", body_style), Paragraph("8080", body_style), Paragraph("ridelink_account", body_style), Paragraph("User authentication, profile management, role assignment", body_style)],
        [Paragraph("Ride Management Service", body_style), Paragraph("8081", body_style), Paragraph("ridelink_ride", body_style), Paragraph("Ride request lifecycle, booking status, driver assignment", body_style)],
        [Paragraph("Driver & Vehicle Service", body_style), Paragraph("8082", body_style), Paragraph("ridelink_driver_vehicle", body_style), Paragraph("Driver profile, license tracking, vehicle registration, location & availability", body_style)],
        [Paragraph("Fare & Payment Service", body_style), Paragraph("8083", body_style), Paragraph("ridelink_fare_payment", body_style), Paragraph("Fare calculation, fare estimation, payment execution (Cash/Card)", body_style)],
    ]
    t_arch = Table(arch_table_data, colWidths=[130, 45, 125, 204])
    t_arch.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,0), colors.HexColor("#EDF2F7")),
        ('GRID', (0,0), (-1,-1), 0.5, colors.HexColor("#CBD5E0")),
        ('PADDING', (0,0), (-1,-1), 5),
        ('VALIGN', (0,0), (-1,-1), 'TOP'),
    ]))
    story.append(t_arch)
    story.append(Spacer(1, 20))

    # --- SECTIONS 2 to 5: SOURCE CODE PER SERVICE ---
    services_info = [
        {
            "num": "2",
            "name": "Driver & Vehicle Service Module",
            "folder": "driver-vehicle-service",
            "member": "Kulasuriya W.N.N (Student ID: IT24104304 - Group Leader)",
            "desc": "Manages driver profiles, license numbers, location coordinates, availability statuses (AVAILABLE, UNAVAILABLE, OFFLINE), vehicle registration, and status lifecycle."
        },
        {
            "num": "3",
            "name": "Ride Management Service Module",
            "folder": "ride-management-service",
            "member": "Perera H.G.K.D (Student ID: IT24104143)",
            "desc": "Handles ride request creation, passenger and driver tracking, location pickup/dropoff points, ride status state machine (REQUESTED, ACCEPTED, IN_PROGRESS, COMPLETED, CANCELLED)."
        },
        {
            "num": "4",
            "name": "Account Service Module",
            "folder": "account-service",
            "member": "Bandara J.K.T.I (Student ID: IT24104276)",
            "desc": "Provides core user identity management, user creation, credentials verification, profile updates, and role management (PASSENGER, DRIVER, ADMIN)."
        },
        {
            "num": "5",
            "name": "Fare & Payment Service Module",
            "folder": "fare-payment-service",
            "member": "Karunarathna H.W.D.S.S (Student ID: IT24104353)",
            "desc": "Calculates estimated and final ride fares based on distance and duration metrics, generates fare receipts, and processes payment transactions (CASH, CARD)."
        },
    ]

    base_dir = r"c:\Users\Asus\Desktop\RideLinkNelundi\RideLink"

    for svc in services_info:
        story.append(PageBreak())
        story.append(Paragraph(f"{svc['num']}. {svc['name']}", h1_style))
        story.append(Paragraph(f"<b>Assigned Lead:</b> {svc['member']}", body_style))
        story.append(Paragraph(f"<b>Overview:</b> {svc['desc']}", body_style))
        story.append(HRFlowable(width="100%", thickness=1, color=colors.HexColor("#CBD5E0"), spaceBefore=4, spaceAfter=12))

        svc_path = os.path.join(base_dir, svc['folder'])
        if not os.path.exists(svc_path):
            continue

        # Collect source files
        files_to_include = []
        for root, dirs, files in os.walk(svc_path):
            if 'target' in root or '.git' in root or '.idea' in root:
                continue
            for f in sorted(files):
                if f.endswith('.java') or f.endswith('.properties') or f == 'pom.xml':
                    rel_path = os.path.relpath(os.path.join(root, f), svc_path)
                    files_to_include.append((rel_path, os.path.join(root, f)))

        files_to_include.sort(key=lambda x: (0 if x[0].endswith('.properties') else 1 if x[0] == 'pom.xml' else 2, x[0]))

        for rel_p, full_p in files_to_include:
            story.append(Paragraph(f"📄 <b>File:</b> <code>{svc['folder']}/{rel_p}</code>", h2_style))
            try:
                with open(full_p, 'r', encoding='utf-8', errors='ignore') as code_f:
                    code_content = code_f.read()
                
                # Split large code blocks into smaller chunks to fit page height
                lines = code_content.split('\n')
                chunk_size = 40
                for i in range(0, len(lines), chunk_size):
                    chunk_text = '\n'.join(lines[i:i+chunk_size])
                    chunk_text = chunk_text.replace('\t', '    ')
                    
                    t_code = Table([[Preformatted(chunk_text, code_style)]], colWidths=[504])
                    t_code.setStyle(TableStyle([
                        ('BACKGROUND', (0,0), (-1,-1), colors.HexColor("#F7FAFC")),
                        ('BOX', (0,0), (-1,-1), 0.5, colors.HexColor("#E2E8F0")),
                        ('PADDING', (0,0), (-1,-1), 4),
                    ]))
                    story.append(t_code)
                    story.append(Spacer(1, 3))
            except Exception as e:
                story.append(Paragraph(f"Error reading file: {e}", body_style))
            story.append(Spacer(1, 8))

    doc.build(story, canvasmaker=NumberedCanvas)
    print("PDF generation complete!")

if __name__ == '__main__':
    build_pdf()
