
#Makefile by Andrew G. West - 7/29/2009
#Slightly modified by Cris Feo 10/8/2012
# Make-file for the CIS400/4001 Project Proposal Specification

	# Filenames to be-used in compilation
MAIN=proposal
TEX=proposal.tex
DVI=proposal.dvi
PS=proposal.ps

	# Core compilation commands
CC=latex
BB=bibtex
BUILDPS=dvips -o $(PS) 
BUILDPDF=ps2pdf

all:$(TEX) $(BIB)
	$(CC) $(TEX)
	$(BB) $(MAIN)
	$(CC) $(TEX)
	$(CC) $(TEX)
	$(BUILDPS) $(DVI)
	$(BUILDPDF) $(PS)

clean:
	rm -rf $(DVI) $(PS) *.log *.aux *.bbl *.blg
