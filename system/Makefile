# This file is part of KeY - Integrated Deductive Software Design 
# Copyright (C) 2001-2004 Universitaet Karlsruhe, Germany
#                         Universitaet Koblenz-Landau, Germany
#                         Chalmers University of Technology, Sweden 
#
# The KeY system is protected by the GNU General Public License. 
# See LICENSE.TXT for details.

SHELL=/bin/sh
#comon prefix
PFX=de/uka/ilkd/key/

#
#the path where to put the binaries.  This should be different from
#the source path (.), otherwise `make clean' will delete the 
#source tree.
BINARYPATH=binary/

# generated source path
export GENERATED_SRC_PATH=genSrc/

#the path where the resources are put (images etc.)
RESOURCEPATH=resources/

#include configuration information
sinclude ./Makefile.mk

ifeq "$(JAVA)" ""
JAVA=$(shell which java)
endif

# enables optimization and removes debug info (such as line tables)
#COMMON_OPTIONS=-O -g:none -d $(BINARYPATH)

COMMON_OPTIONS=-g -d $(BINARYPATH)
OPTIONS=$(COMMON_OPTIONS)



GIT_AVAILABLE=$(shell which git)

ifneq ($(strip $(wordlist 1,1,$(GIT_AVAILABLE))),no) 
GIT=$(shell which git)
endif

GIT_REPOS_AVAILABLE=$(wildcard ../.git/objects)

SHA1=ownVersion

ifeq "$(KEY_VERSION_SHORT)" "" 
  KEY_VERSION_SHORT=0.0.0
endif

ifneq "$(GIT)" "" 
 ifneq "$(GIT_REPOS_AVAILABLE)" ""
   SHA1:=$(shell $(GIT) show | grep commit)
   SHA1:=$(wordlist 2,2,$(SHA1))
   ifeq "$(KEY_VERSION_SHORT)" "0.0.0" 
     KEY_VERSION_SHORT:=$(shell $(GIT) log --pretty=one | wc -l)
     KEY_VERSION_SHORT:=$(wordlist 1,1,$(KEY_VERSION_SHORT))
     KEY_VERSION_SHORT:=1.5.$(KEY_VERSION_SHORT)
   endif
 endif
endif

ifeq "$(KEY_VERSION_LONG)" ""
KEY_VERSION_LONG=$(KEY_VERSION_SHORT)_$(SHA1)
endif

KEY_VERSION_SHORT=3.6
KEY_VERSION_LONG=3.6.16

export KEY_VERSION_LONG
export KEY_VERSION_SHORT

ifeq "$(JAVAC)" ""
JAVAC=javac
else 
ifeq ($(JAVAC),jikes) 
JAVAC:=javac
endif
endif

ifeq "$(ANTLR)" ""
ANTLR=$(JAVA) -cp $(GENERATED_SRC_PATH):$(CLASSPATH) antlr.Tool
endif

ifeq "$(ANTLR3)" ""
ANTLR3=$(JAVA) -cp $(GENERATED_SRC_PATH):$(CLASSPATH) org.antlr.Tool
endif

ifeq ($(JAVAC),ajc)
  OPTIONS=$(COMMON_OPTIONS) -nowarn -classpath $(ASPECTJRTPATH):$(CLASSPATH):$(GENERATED_SRC_PATH):$(BINARYPATH):.
else
  OPTIONS=$(COMMON_OPTIONS) -J-Xmx512m -nowarn -source 1.6 -target 1.6 -classpath $(CLASSPATH):$(GENERATED_SRC_PATH):$(BINARYPATH):.
endif

JAVACC_MAIN=javacc

## nice source links
JAVADOC=javadoc
JAVADOC_OPTIONS=-use -quiet -breakiterator -linksource -J-Xmx512m


ANTLR_PARSER= \
	$(GENERATED_SRC_PATH)$(PFX)parser/KeYLexer.java \
	$(GENERATED_SRC_PATH)$(PFX)parser/KeYLexerTokenTypes.java \
	$(GENERATED_SRC_PATH)$(PFX)parser/KeYParser.java \
	$(GENERATED_SRC_PATH)$(PFX)parser/dictionary/DictionaryLexer.java \
	$(GENERATED_SRC_PATH)$(PFX)parser/dictionary/DictionaryLexerTokenTypes.java \
	$(GENERATED_SRC_PATH)$(PFX)parser/dictionary/DictionaryParser.java \
	$(GENERATED_SRC_PATH)$(PFX)speclang/jml/pretranslation/KeYJMLPreLexer.java \
	$(GENERATED_SRC_PATH)$(PFX)speclang/jml/pretranslation/KeYJMLPreParser.java \
	$(GENERATED_SRC_PATH)$(PFX)speclang/jml/translation/KeYJMLLexer.java \
	$(GENERATED_SRC_PATH)$(PFX)speclang/jml/translation/KeYJMLParser.java \
	$(GENERATED_SRC_PATH)$(PFX)parser/simplify/SimplifyLexer.java \
	$(GENERATED_SRC_PATH)$(PFX)parser/simplify/SimplifyParser.java \
	$(GENERATED_SRC_PATH)$(PFX)speclang/ocl/translation/KeYOCLLexer.java \
	$(GENERATED_SRC_PATH)$(PFX)speclang/ocl/translation/KeYOCLParser.java \
	$(GENERATED_SRC_PATH)$(PFX)util/keydoc/parser/KeYDocLexer.java \
	$(GENERATED_SRC_PATH)$(PFX)util/keydoc/parser/KeYDocParser.java \
	$(GENERATED_SRC_PATH)$(PFX)util/keydoc/parser/KeYDocTreeWalker.java \
	$(GENERATED_SRC_PATH)$(PFX)dl/parser/DLParser.java \
	$(GENERATED_SRC_PATH)$(PFX)dl/parser/DLStage2.java \
	$(GENERATED_SRC_PATH)$(PFX)dl/parser/QepcadParser.java \
	$(GENERATED_SRC_PATH)$(PFX)dl/parser/ReduceParser.java 



JAVACC_PARSER= \
	$(GENERATED_SRC_PATH)$(PFX)parser/schemajava/SchemaJavaParserConstants.java \
	$(GENERATED_SRC_PATH)$(PFX)parser/schemajava/ParseException.java \
	$(GENERATED_SRC_PATH)$(PFX)parser/schemajava/TokenMgrError.java \
	$(GENERATED_SRC_PATH)$(PFX)parser/schemajava/SchemaJavaParserTokenManager.java \
	$(GENERATED_SRC_PATH)$(PFX)parser/schemajava/SchemaJavaParser.java \
	$(GENERATED_SRC_PATH)$(PFX)parser/schemajava/Token.java 

PROOFJAVA_PARSER= \
	$(GENERATED_SRC_PATH)$(PFX)parser/proofjava/ProofJavaParserConstants.java \
	$(GENERATED_SRC_PATH)$(PFX)parser/proofjava/ParseException.java \
	$(GENERATED_SRC_PATH)$(PFX)parser/proofjava/TokenMgrError.java \
	$(GENERATED_SRC_PATH)$(PFX)parser/proofjava/ProofJavaParserTokenManager.java \
	$(GENERATED_SRC_PATH)$(PFX)parser/proofjava/ProofJavaParser.java \
	$(GENERATED_SRC_PATH)$(PFX)parser/proofjava/Token.java \

DIFF_PARSER= \
	$(GENERATED_SRC_PATH)$(PFX)parser/diffparser/DiffParserConstants.java \
	$(GENERATED_SRC_PATH)$(PFX)parser/diffparser/ParseException.java \
	$(GENERATED_SRC_PATH)$(PFX)parser/diffparser/TokenMgrError.java \
	$(GENERATED_SRC_PATH)$(PFX)parser/diffparser/DiffParserTokenManager.java \
	$(GENERATED_SRC_PATH)$(PFX)parser/diffparser/DiffParser.java \
	$(GENERATED_SRC_PATH)$(PFX)parser/diffparser/Token.java



.PHONY: all
all:	javahomeset branding 
	@$(MAKE) -s all_gen
	@echo [KeYmaera Version $(KEY_VERSION_LONG)]


## create javadoc
.PHONY: doc
doc: argfile 
	@mkdir -p doc
## old doc generation	
#	@find de/ | grep "\.java" | grep -v "\~" | xargs javadoc -use -classpath $(CLASSPATH):$(BINARYPATH) -d doc 

## new one produces better output (package index) but is more fragile.
## didn't want to use perl ;)
	@find de/ -name '*.java' | xargs grep -h "^package\|^overview" |\
	cut -d" " -f2 | cut -d";" -f1 | sort | uniq | grep "^de.uka" | \
	xargs $(JAVADOC) $(JAVADOC_OPTIONS) -source 1.5 -classpath $(CLASSPATH):$(GENERATED_SRC_PATH):$(BINARYPATH) \
	-sourcepath $(GENERATED_SRC_PATH):. -d doc \
	-link http://recoder.sourceforge.net/doc/api \
	-link http://java.sun.com/j2se/1.5.0/docs/api/ \
	-link http://www.antlr.org \
	-windowtitle "KeY API Documentation" \
	-doctitle "KeY API Documentation ($(KEY_VERSION_LONG))" \
	-header "KeY $(KEY_VERSION_LONG)" \
	-private -author -version -J-Xmx256m



## create KeY distribution package
.PHONY: dist
dist:	branding all
	@echo "[creating distribution directory structure]"
	@echo [KeYmaera Version $(KEY_VERSION_LONG)]
	@mkdir -p ../dist
	@mkdir -p dist/key/
	@cp -r $(BINARYPATH)/* dist/key/
	@cp -r $(RESOURCEPATH)/* dist/key/
	@cp -r ../examples dist/key/
# for setup.jar
	@mkdir -p dist/setup/de/uka/ilkd/key/util/
	@cp -r $(BINARYPATH)/de/uka/ilkd/key/util/* dist/setup/de/uka/ilkd/key/util/
	@cp -r $(RESOURCEPATH)/de/uka/ilkd/key/util/install/* dist/setup/de/uka/ilkd/key/util/install/
	@mkdir -p dist/setup/de/uka/ilkd/key/gui/
	@cp -r $(BINARYPATH)/de/uka/ilkd/key/gui/IconFactory* dist/setup/de/uka/ilkd/key/gui/
	@cp -r $(BINARYPATH)/de/uka/ilkd/key/gui/KeYFolderIcon* dist/setup/de/uka/ilkd/key/gui/
	@cp -r $(BINARYPATH)/de/uka/ilkd/key/gui/KeYControlIcon* dist/setup/de/uka/ilkd/key/gui/
	@cp -r $(RESOURCEPATH)/de/uka/ilkd/key/gui/* dist/setup/de/uka/ilkd/key/gui/
	@find dist -name '*~' -exec rm \{\} \; 
	@echo "[creating jar-file]"
	@jar cfm key.jar resources/MANIFEST.MF -C dist/key/ .
	@jar cfm setup.jar resources/de/uka/ilkd/key/util/install/MANIFEST.MF -C dist/setup/ .
	@tar -czvf ../dist/KeY-$(KEY_VERSION_LONG).tgz key.jar setup.jar -C ../ LICENSE.TXT
	@cp ../README.install.bytecode ../dist/README-$(KEY_VERSION_LONG).txt
	@echo "[deleting distribution directory structure]"
	@rm -rf dist

OPTIONS:=${OPTIONS} -target 1.6

## create KeYmaera distribution package
dist-keymaera:	branding all
	@echo "[creating distribution directory structure]"
	@mkdir -p ../dist
	@mkdir -p dist/key/
	@cp -r $(BINARYPATH)/* dist/key/
	@cp -r $(RESOURCEPATH)/* dist/key/
#copy example file
	@for i in `grep --no-filename "\<path\>" $(RESOURCEPATH)description.xml $(RESOURCEPATH)tutorial.xml | grep -v 'http://' | sed s/'<path>\/'// | sed s/'<\/path>'// | sed 's/^[ \t]*//'`; do \
		mkdir -p dist/key/`dirname $$i`;\
		cp -RH ../$$i dist/key/$$i;\
		done
#copy example proof files
	@for i in `grep --no-filename 'href=.*.proof' $(RESOURCEPATH)description.xml $(RESOURCEPATH)tutorial.xml | grep -v 'http://' | sed s/'.*href=\"\/'// | sed s/'\".*'// | sed 's/^[ \t]*//'`; do \
		mkdir -p dist/key/`dirname $$i`;\
		cp -RH ../$$i dist/key/$$i;\
		done
#copy images
	@for i in `grep --no-filename '\<img>' $(RESOURCEPATH)description.xml $(RESOURCEPATH)tutorial.xml | grep -v 'http://' | sed s/'<img>\/'// | sed s/'<\/img>'// | sed 's/^[ \t]*//'`; do \
		mkdir -p dist/key/`dirname $$i`;\
		cp -RH ../$$i dist/key/$$i;\
		done
# for setup.jar
	@mkdir -p dist/setup/de/uka/ilkd/key/util/
	@cp -r $(BINARYPATH)/de/uka/ilkd/key/util/* dist/setup/de/uka/ilkd/key/util/
	@cp -r $(RESOURCEPATH)/de/uka/ilkd/key/util/install/* dist/setup/de/uka/ilkd/key/util/install/
	@mkdir -p dist/setup/de/uka/ilkd/key/gui/
	@cp -r $(BINARYPATH)/de/uka/ilkd/key/gui/IconFactory* dist/setup/de/uka/ilkd/key/gui/
	@cp -r $(BINARYPATH)/de/uka/ilkd/key/gui/KeYFolderIcon* dist/setup/de/uka/ilkd/key/gui/
	@cp -r $(BINARYPATH)/de/uka/ilkd/key/gui/KeYControlIcon* dist/setup/de/uka/ilkd/key/gui/
	@cp -r $(RESOURCEPATH)/de/uka/ilkd/key/gui/* dist/setup/de/uka/ilkd/key/gui/
	@find dist -name '*~' -exec rm \{\} \; 
	@echo "[creating jar-file]"
	@jar cfm keymaera.jar resources/MANIFEST.MF -C dist/key/ .
	@jar cfm setup.jar resources/de/uka/ilkd/key/util/install/MANIFEST.MF -C dist/setup/ .
	@tar -czvf ../dist/KeY-$(KEY_VERSION_LONG).tgz keymaera.jar setup.jar -C ../ LICENSE.TXT
	@cp ../README.install.bytecode ../dist/README-$(KEY_VERSION_LONG).txt
	@echo "[distribution files created in dist/KeY-$(KEY_VERSION_LONG).tgz]"
	@echo "[deleting distribution directory structure]"
	@rm -rf dist

.PHONY: jar
jar:	all
	@echo "[creating jar-file]"
	@jar cfm key.jar resources/MANIFEST.MF -C binary/ .

.PHONY: dist_src
dist_src: distclean branding javaFiles
	@rm -rf /tmp/keydist-$(USER)
	@echo "[creating source archive]"
	@mkdir -p /tmp/keydist-$(USER)
	@ln -s $(shell cd ..;pwd) /tmp/keydist-$(USER)/key-$(KEY_VERSION_LONG)
	@tar -C/tmp/keydist-$(USER) --dereference  \
        -cvf /tmp/keydist-$(USER)/KeY-$(KEY_VERSION_LONG)-src.tar --exclude=key-$(KEY_VERSION_LONG)/.git/'*' \
	--exclude=key-$(KEY_VERSION_LONG)/key-ext-jars/'*' --exclude=key-$(KEY_VERSION_LONG)/eclipse key-$(KEY_VERSION_LONG)
	@tar -rf /tmp/keydist-$(USER)/KeY-$(KEY_VERSION_LONG)-src.tar -C /tmp/keydist-$(USER) key-$(KEY_VERSION_LONG)/eclipse/
	@gzip /tmp/keydist-$(USER)/KeY-$(KEY_VERSION_LONG)-src.tar
	@mv /tmp/keydist-$(USER)/KeY-$(KEY_VERSION_LONG)-src.tar.gz ../KeY-$(KEY_VERSION_LONG)-src.tgz
	@cp /tmp/keydist-$(USER)/key-$(KEY_VERSION_LONG)/README.install.source ../README-$(KEY_VERSION_LONG)-src.txt
	@echo "[done]"


.PHONY: clean
## clean
clean:
	@echo [deleting generated .java-files]
	@/bin/rm -rf $(GENERATED_SRC_PATH)
	@echo [deleting binaries]
	@/bin/rm -rf $(BINARYPATH)

#help files of the makefile (without the GenMakefile)

	@rm -f javaFiles
	@rm -f unit_manager.log 
	@rm -f msg.txt
	@rm -f argfile.aspects
	@rm -f argfile.normal 
	@rm -f argfile.other
	@rm -f argfile
	@rm -f error.msg
	@rm -f critical.msg
	@rm -f warning.msg
	@rm -f -r ./qwe

.PHONY: realclean
realclean: clean

#help files of the makefile
	@rm -f Makefile.mk

.PHONY: distclean
distclean: realclean
	@rm -f setup.jar
	@rm -f key.jar
	@rm -f KeY.tgz
	@rm -f keySource.tgz

.PHONY: all_gen
all_gen: 
	@$(MAKE) -s all_hlp   	

COHENHORMANDERCLASS = $(BINARYPATH)$(PFX)dl/arithmetics/impl/ch/cohenhormander/AM.class

.PHONY: all_hlp
all_hlp: argfile.normal $(ANTLR_PARSER) $(JAVACC_PARSER) $(PROOFJAVA_PARSER) $(KEYDOC_JAVAS) $(DIFF_PARSER) copyResources $(COHENHORMANDERCLASS)  #cohenhormander
ifeq "$(BRANDING)" ""
# always update SHA1 hash in order to get useful bug reports		
	@echo $(SHA1) > ${BINARYPATH}/de/uka/ilkd/key/util/sha1
	@echo $(KEY_VERSION_SHORT) > ${BINARYPATH}/de/uka/ilkd/key/util/version
else
endif
	@rm -f error.msg
	@rm -f critical.msg
	@rm -f warning.msg
	@echo [KeY is being compiled]
	@echo $(ANTLR_PARSER) $(JAVACC_PARSER) \
	      $(PROOFJAVA_PARSER) $(DIFF_PARSER) \
              | xargs -n1 echo > argfile.other 
ifeq ($(JAVAC),ajc)
	@grep -v "^#" aspects/aspects.config > argfile.aspects
else
	@rm -f argfile.aspects
	@touch argfile.aspects
endif	
	@cat argfile.normal argfile.other argfile.aspects | sort -u > argfile
	@$(JAVAC) $(OPTIONS) @argfile 2>>error.msg || true
	@./success

.PHONY: copyResources
copyResources: javahomeset
	@echo "[copying resources]"
	@cp -r $(RESOURCEPATH)/* $(BINARYPATH)


branding:
ifeq "$(BRANDING)" ""
	@echo "[No Branding. Set environment variable BRANDING to activate branding.]"
else
	@echo "[Branding]"
	@echo $(SHA1) > resources/de/uka/ilkd/key/util/sha1
	@echo $(KEY_VERSION_SHORT) > resources/de/uka/ilkd/key/util/version
	@./brandManifest.sh ../eclipse/KeYExternalLibraries/META-INF/MANIFEST.MF $(KEY_VERSION_SHORT)
	@./brandManifest.sh ../eclipse/org.key-project.core/META-INF/MANIFEST.MF $(KEY_VERSION_SHORT)
	@./brandManifest.sh ../eclipse/KeYPlugin/META-INF/MANIFEST.MF $(KEY_VERSION_SHORT)
	@./brandManifest.sh ../eclipse/VisualDebugger/META-INF/MANIFEST.MF $(KEY_VERSION_SHORT)
	@./brandPlugin.sh ../eclipse/ProofVisualization/plugin.xml $(KEY_VERSION_SHORT)
# we must replace the feature url and version tag in site.xml 
	@./brandPlugin.sh ../eclipse/KeYFeature/site.xml $(KEY_VERSION_SHORT)
	@./brandPlugin.sh ../eclipse/KeYFeature/feature.xml $(KEY_VERSION_SHORT)
	@./brandFeatureSite.sh ../eclipse/KeYFeature/site.xml $(KEY_VERSION_SHORT)
endif

javaFiles: 
ifneq ("$(GIT)","")
	@$(GIT) ls-files | grep "\.java" | grep -v "aspects" | grep -v "gjava" | grep -v "\.java\." | grep -v "resources/" | sort > javaFiles
endif


argfile.normal: javaFiles
	@echo [Collecting Java files to compile]
	@cat javaFiles > argfile.normal
## make gjava -> java


$(JAVACC_PARSER) \
	: $(PFX)parser/schemajava/SchemaJavaParser.jj
	@echo [creating parser for schematic java in taclets]
	@mkdir -p $(GENERATED_SRC_PATH)$(PFX)parser/schemajava
	@rm -f $(GENERATED_SRC_PATH)$(PFX)parser/schemajava/*.java $@
	@cp $(PFX)parser/schemajava/Token.java.source $(GENERATED_SRC_PATH)$(PFX)parser/schemajava/Token.java	
	@$(JAVA) -cp $(CLASSPATH) $(JAVACC_MAIN) -OUTPUT_DIRECTORY=$(GENERATED_SRC_PATH)$(PFX)parser/schemajava $< 

$(PROOFJAVA_PARSER) \
	: $(PFX)parser/proofjava/ProofJavaParser.jj
	@echo [create parser for extended java in proofs]
	@mkdir -p $(GENERATED_SRC_PATH)$(PFX)parser/proofjava
	@rm -f $(GENERATED_SRC_PATH)$(PFX)parser/proofjava/*.java $@
	@cp $(PFX)parser/proofjava/Token.java.source $(GENERATED_SRC_PATH)$(PFX)parser/proofjava/Token.java
	@$(JAVA) -cp $(CLASSPATH) $(JAVACC_MAIN) -OUTPUT_DIRECTORY=$(GENERATED_SRC_PATH)$(PFX)parser/proofjava $< 

$(DIFF_PARSER) \
	: $(PFX)parser/diffparser/DiffParser.jj
	@echo [creating unified diff parser]
	@mkdir -p $(GENERATED_SRC_PATH)$(PFX)parser/diffparser
	@rm -f $(GENERATED_SRC_PATH)$(PFX)parser/diffparser/*.java $@
	@$(JAVA) -cp $(CLASSPATH) $(JAVACC_MAIN) -OUTPUT_DIRECTORY=$(GENERATED_SRC_PATH)$(PFX)parser/diffparser $< 

$(GENERATED_SRC_PATH)$(PFX)parser/dictionary/DictionaryLexer.java \
$(GENERATED_SRC_PATH)$(PFX)parser/dictionary/DictionaryLexerTokenTypes.txt \
$(GENERATED_SRC_PATH)$(PFX)parser/dictionary/DictionaryLexerTokenTypes.java \
	: $(PFX)parser/dictionary/dictionaryLexer.g
	@echo [creating DictionaryLexer]
	@mkdir -p $(GENERATED_SRC_PATH)$(PFX)parser/dictionary
	@$(ANTLR)  -o $(GENERATED_SRC_PATH)$(PFX)parser/dictionary $<

$(GENERATED_SRC_PATH)$(PFX)parser/dictionary/DictionaryParser.java \
	: $(PFX)parser/dictionary/dictionary.g \
	  $(GENERATED_SRC_PATH)$(PFX)parser/dictionary/DictionaryLexerTokenTypes.txt
	@echo [creating dictionary parser]
	@mkdir -p $(GENERATED_SRC_PATH)$(PFX)parser/dictionary
	@$(ANTLR)  -glib $(PFX)parser/dictionary/dictionaryLexer.g \
			 -o $(GENERATED_SRC_PATH)$(PFX)parser/dictionary $<

$(GENERATED_SRC_PATH)$(PFX)parser/KeYLexer.java \
$(GENERATED_SRC_PATH)$(PFX)parser/KeYLexerTokenTypes.txt \
$(GENERATED_SRC_PATH)$(PFX)parser/KeYLexerTokenTypes.java \
	: $(PFX)parser/lexer.g
	@echo [creating KeYLexer]
	@mkdir -p $(GENERATED_SRC_PATH)$(PFX)parser
	@$(ANTLR)  -o $(GENERATED_SRC_PATH)$(PFX)parser $<

$(GENERATED_SRC_PATH)$(PFX)parser/KeYParser.java \
	: $(PFX)parser/keyparser.g \
	  $(GENERATED_SRC_PATH)$(PFX)parser/KeYLexerTokenTypes.txt
	@echo [creating global KeY parser]
	@mkdir -p $(GENERATED_SRC_PATH)$(PFX)parser
	@$(ANTLR) -glib $(PFX)parser/lexer.g \
			 -o $(GENERATED_SRC_PATH)$(PFX)parser $<

DL_PFX=$(PFX)dl/parser/
DL_GEN_PFX=$(GENERATED_SRC_PATH)$(DL_PFX)

$(DL_GEN_PFX)DLParser.java $(DL_GEN_PFX)DL.tokens: $(DL_PFX)DL.g
	@echo [creating DLParser Stage 1]
	@mkdir -p $(DL_GEN_PFX)
	@$(ANTLR3) -lib $(DL_GEN_PFX) -o $(GENERATED_SRC_PATH) $<

$(DL_GEN_PFX)DLStage2.java: $(DL_PFX)DLStage2.g $(DL_GEN_PFX)DL.tokens
	@echo [creating DLParser Stage 2]
	@mkdir -p $(DL_GEN_PFX)
	@$(ANTLR3) -lib $(DL_GEN_PFX) -o $(GENERATED_SRC_PATH) $<

$(DL_GEN_PFX)QepcadParser.java: $(DL_PFX)Qepcad.g $(DL_GEN_PFX)DL.tokens
	@echo [creating Qepcad parser]
	@mkdir -p $(DL_GEN_PFX)
	@$(ANTLR3) -lib $(DL_GEN_PFX) -o $(GENERATED_SRC_PATH) $<

$(DL_GEN_PFX)ReduceParser.java: $(DL_PFX)Reduce.g $(DL_GEN_PFX)DL.tokens
	@echo [creating Reduce parser]
	@mkdir -p $(DL_GEN_PFX)
	@$(ANTLR3) -lib $(DL_GEN_PFX) -o $(GENERATED_SRC_PATH) $<

$(GENERATED_SRC_PATH)$(PFX)parser/simplify/SimplifyLexer.java \
$(GENERATED_SRC_PATH)$(PFX)parser/simplify/SimplifyLexerTokenTypes.txt \
$(GENERATED_SRC_PATH)$(PFX)parser/simplify/SimplifyLexerTokenTypes.java \
	: $(PFX)parser/simplify/lexer.g
	@echo [creating SimplifyLexer]
	@mkdir -p $(GENERATED_SRC_PATH)$(PFX)parser/simplify
	@$(JAVA) -cp $(CLASSPATH) antlr.Tool  -o $(GENERATED_SRC_PATH)$(PFX)parser/simplify $<

$(GENERATED_SRC_PATH)$(PFX)parser/simplify/SimplifyParser.java \
	: $(PFX)parser/simplify/simplify.g \
	  $(GENERATED_SRC_PATH)$(PFX)parser/simplify/SimplifyLexerTokenTypes.txt
	@echo [creating Simplify parser]
	@mkdir -p $(GENERATED_SRC_PATH)$(PFX)parser/simplify
	@$(JAVA) -cp $(CLASSPATH) antlr.Tool  -glib $(PFX)parser/simplify/lexer.g \
			 -o $(GENERATED_SRC_PATH)$(PFX)parser/simplify $<

$(GENERATED_SRC_PATH)$(PFX)speclang/ocl/translation/KeYOCLLexer.java \
$(GENERATED_SRC_PATH)$(PFX)speclang/ocl/translation/KeYOCLLexerTokenTypes.txt \
$(GENERATED_SRC_PATH)$(PFX)speclang/ocl/translation/KeYOCLLexerTokenTypes.java \
	: $(PFX)speclang/ocl/translation/ocllexer.g
	@echo [creating OCL lexer]
	@mkdir -p $(GENERATED_SRC_PATH)$(PFX)speclang/ocl/translation
	@$(JAVA) -cp $(CLASSPATH) antlr.Tool  -o $(GENERATED_SRC_PATH)$(PFX)speclang/ocl/translation $<

$(GENERATED_SRC_PATH)$(PFX)speclang/ocl/translation/KeYOCLParser.java \
	: $(PFX)speclang/ocl/translation/oclparser.g \
	  $(GENERATED_SRC_PATH)$(PFX)speclang/ocl/translation/KeYOCLLexerTokenTypes.txt
	@echo [creating OCL parser]
	@mkdir -p $(GENERATED_SRC_PATH)$(PFX)speclang/ocl/translation
	@$(JAVA) -cp $(CLASSPATH) antlr.Tool  -glib $(PFX)speclang/ocl/translation/ocllexer.g \
			 -o $(GENERATED_SRC_PATH)$(PFX)speclang/ocl/translation $<

$(GENERATED_SRC_PATH)$(PFX)speclang/jml/pretranslation/KeYJMLPreLexer.java \
$(GENERATED_SRC_PATH)$(PFX)speclang/jml/pretranslation/KeYJMLPreLexerTokenTypes.txt \
$(GENERATED_SRC_PATH)$(PFX)speclang/jml/pretranslation/KeYJMLPreLexerTokenTypes.java \
	: $(PFX)speclang/jml/pretranslation/jmlprelexer.g
	@echo [creating JML pre lexer]
	@mkdir -p $(GENERATED_SRC_PATH)$(PFX)speclang/jml/pretranslation
	@$(JAVA) -cp $(CLASSPATH) antlr.Tool  -o $(GENERATED_SRC_PATH)$(PFX)speclang/jml/pretranslation $<

$(GENERATED_SRC_PATH)$(PFX)speclang/jml/pretranslation/KeYJMLPreParser.java \
	: $(PFX)speclang/jml/pretranslation/jmlpreparser.g \
	  $(GENERATED_SRC_PATH)$(PFX)speclang/jml/pretranslation/KeYJMLPreLexerTokenTypes.txt
	@echo [creating JML pre parser]
	@mkdir -p $(GENERATED_SRC_PATH)$(PFX)speclang/jml/pretranslation
	@$(JAVA) -cp $(CLASSPATH) antlr.Tool  -glib $(PFX)speclang/jml/pretranslation/jmlprelexer.g \
			 -o $(GENERATED_SRC_PATH)$(PFX)speclang/jml/pretranslation $<

$(GENERATED_SRC_PATH)$(PFX)speclang/jml/translation/KeYJMLLexer.java \
$(GENERATED_SRC_PATH)$(PFX)speclang/jml/translation/KeYJMLLexerTokenTypes.txt \
$(GENERATED_SRC_PATH)$(PFX)speclang/jml/translation/KeYJMLLexerTokenTypes.java \
	: $(PFX)speclang/jml/translation/jmllexer.g
	@echo [creating JML lexer]
	@mkdir -p $(GENERATED_SRC_PATH)$(PFX)speclang/jml/translation
	@$(JAVA) -cp $(CLASSPATH) antlr.Tool  -o $(GENERATED_SRC_PATH)$(PFX)speclang/jml/translation $<

$(GENERATED_SRC_PATH)$(PFX)speclang/jml/translation/KeYJMLParser.java \
	: $(PFX)speclang/jml/translation/jmlparser.g \
	  $(GENERATED_SRC_PATH)$(PFX)speclang/jml/translation/KeYJMLLexerTokenTypes.txt
	@echo [creating JML parser]
	@mkdir -p $(GENERATED_SRC_PATH)$(PFX)speclang/jml/translation
	@$(JAVA) -cp $(CLASSPATH) antlr.Tool  -glib $(PFX)speclang/jml/translation/jmllexer.g \
	                 -o $(GENERATED_SRC_PATH)$(PFX)speclang/jml/translation $<

$(GENERATED_SRC_PATH)$(PFX)util/keydoc/parser/KeYDocLexer.java \
$(GENERATED_SRC_PATH)$(PFX)util/keydoc/parser/KeYDocLexerTokenTypes.txt \
$(GENERATED_SRC_PATH)$(PFX)util/keydoc/parser/KeYDocLexerTokenTypes.java \
	: $(PFX)util/keydoc/parser/KeYDocLexer.g
	@echo [creating KeYDoc Lexer]
	@mkdir -p $(GENERATED_SRC_PATH)$(PFX)util/keydoc/parser
	@$(ANTLR)  -o $(GENERATED_SRC_PATH)$(PFX)util/keydoc/parser $<

$(GENERATED_SRC_PATH)$(PFX)util/keydoc/parser/KeYDocParser.java \
$(GENERATED_SRC_PATH)$(PFX)util/keydoc/parser/KeYDocParserTokenTypes.txt \
$(GENERATED_SRC_PATH)$(PFX)util/keydoc/parser/KeYDocParserTokenTypes.java \
	: $(PFX)util/keydoc/parser/KeYDocParser.g \
	  $(GENERATED_SRC_PATH)$(PFX)util/keydoc/parser/KeYDocLexerTokenTypes.txt 
	@echo [creating KeYDoc Parser]
	@mkdir -p $(GENERATED_SRC_PATH)$(PFX)util/keydoc/parser
	@$(ANTLR)  -glib $(PFX)util/keydoc/parser/KeYDocLexer.g -o $(GENERATED_SRC_PATH)$(PFX)util/keydoc/parser $<

$(GENERATED_SRC_PATH)$(PFX)util/keydoc/parser/KeYDocTreeWalker.java \
	: $(PFX)util/keydoc/parser/KeYDocTreeWalker.g \
	  $(GENERATED_SRC_PATH)$(PFX)util/keydoc/parser/KeYDocParserTokenTypes.txt 
	@echo [creating KeYDoc Treewalker]
	@mkdir -p $(GENERATED_SRC_PATH)$(PFX)util/keydoc/parser
	@$(ANTLR)  -glib $(PFX)util/keydoc/parser/KeYDocParser.g -o $(GENERATED_SRC_PATH)$(PFX)util/keydoc/parser $<

.PHONY: help
help:
	@echo
	@echo " (g)make"
	@echo "    help:      prints this message"
	@echo
	@echo "    all:       compiles the KeY-system"
	@echo
	@echo "    dist:      'all' + creates a binary distribution "
	@echo "               (../KeY.tgz containing an installation script, "
	@echo "               the compiled KeY-system without sources, "
	@echo "               without external libraries)"
	@echo
	@echo "    dist_src:  'realclean' + creates a sourcecode distribution"
	@echo "               (../KeY.src.tgz containing all source files but "
	@echo "               without the external libraries)" 
	@echo
	@echo "    clean:     deletes all generated .java and .class files"
	@echo "               but not the configuration of ext. libraries"
	@echo
	@echo "    realclean: 'clean' + deletes libraries configuration"
	@echo
	@echo "    doc:       creates the documentation of the KeY-system"
	@echo
	@echo "    eclipse-plug-in: 'dist' + builds the eclipse plug-in"
	@echo "                     needs ECLIPSE_HOME to be pointing to your"
	@echo "                     eclipse application"

.PHONY: javahomeset
javahomeset:
	@./checkEnvironment
	@mkdir -p $(BINARYPATH)

# eclipse plug-in:
eclipse-plug-in: dist
	@cd ../eclipse/KeYFeature && $(MAKE)
	@rm -fr $(BINARYPATH)$(PFX)casetool/eclipse
	@mkdir -p $(BINARYPATH)$(PFX)casetool/eclipse/KeY_Feature/features
	@mkdir -p $(BINARYPATH)$(PFX)casetool/eclipse/KeY_Feature/plugins
	@cp  ../eclipse/KeYFeature/site.xml $(BINARYPATH)$(PFX)casetool/eclipse/KeY_Feature/ 
	@cp  ../eclipse/KeYFeature/KeY_Feature_*.jar $(BINARYPATH)$(PFX)casetool/eclipse/KeY_Feature/features/
	@cp  ../eclipse/KeYPlugin/KeYPlugin_*.jar $(BINARYPATH)$(PFX)casetool/eclipse/KeY_Feature/plugins/
	@cp  ../eclipse/ProofVisualization/ProofVisualization_*.jar $(BINARYPATH)$(PFX)casetool/eclipse/KeY_Feature/plugins
	@cp  ../eclipse/org.key-project.core/org.key_project.core_* $(BINARYPATH)$(PFX)casetool/eclipse/KeY_Feature/plugins
	@cp  ../eclipse/KeYExternalLibraries/KeYExternalLibraries_1* $(BINARYPATH)$(PFX)casetool/eclipse/KeY_Feature/plugins


SCALAC = scalac
SCALAOPTIONS =  -deprecation -d $(BINARYPATH) -classpath $(CLASSPATH)

COHENHORMANDER = $(PFX)dl/arithmetics/impl/ch/cohenhormander
METIT = $(PFX)/dl/arithmetics/impl/metitarski

SCALAFILES = $(COHENHORMANDER)/parallel.scala $(COHENHORMANDER)/parse.scala \
	$(COHENHORMANDER)/rational.scala $(COHENHORMANDER)/arithmetic.scala \
	$(PFX)dl/formulatools/Derive.scala $(PFX)dl/formulatools/PolynomialExtraction.scala $(PFX)dl/formulatools/NegationNormalForm.scala $(PFX)dl/formulatools/RichTerm.scala $(PFX)dl/arithmetics/impl/reduce/Term2ReduceString.scala \
	$(PFX)dl/formulatools/Prenex.scala $(PFX)dl/arithmetics/impl/mathematica/TermExprLink.scala \
	$(PFX)dl/strategy/tactics/SkolemizeTactic.scala $(PFX)dl/strategy/tactics/ApplyEquationTactic.scala \
	$(METIT)/FormulaTree.scala $(METIT)/OperatorMap.scala $(PFX)dl/gui/AutoModeDialog.scala \
	../lslabcore/edu/cmu/cs/ls/hp.scala ../lslabcore/edu/cmu/cs/ls/lyusimul/evolveToExpr.scala ../lslabcore/edu/cmu/cs/ls/lyusimul/exprToPlottableString.scala ../lslabcore/edu/cmu/cs/ls/lyusimul/formulaToPlot.scala ../lslabcore/edu/cmu/cs/ls/lyusimul/hpToExpr.scala ../lslabcore/edu/cmu/cs/ls/lyusimul/main.scala ../lslabcore/edu/cmu/cs/ls/lyusimul/mathematicaPlot.scala ../lslabcore/edu/cmu/cs/ls/lyusimul/mmt.scala ../lslabcore/edu/cmu/cs/ls/lyusimul/mmtManipulation.scala ../lslabcore/edu/cmu/cs/ls/lyusimul/nameMasker.scala ../lslabcore/edu/cmu/cs/ls/package.scala ../lslabcore/edu/cmu/cs/ls/parse.scala ../lslabcore/edu/cmu/cs/ls/parseoriginal.scala ../lslabcore/edu/cmu/cs/ls/rational.scala ../lslabcore/edu/cmu/cs/ls/types.scala ../lslabcore/edu/cmu/cs/ls/lyusimul/exprConsts.scala


$(COHENHORMANDERCLASS) ch : $(SCALAFILES) argfile.normal
	@echo [compiling scala files]
	$(SCALAC) $(SCALAOPTIONS) @argfile.normal $(SCALAFILES)   


