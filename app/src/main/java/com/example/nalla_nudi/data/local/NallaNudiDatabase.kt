package com.example.nalla_nudi.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.nalla_nudi.model.Term
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Term::class], version = 1, exportSchema = false)
abstract class NallaNudiDatabase : RoomDatabase() {

    abstract fun termDao(): TermDao

    companion object {
        @Volatile
        private var INSTANCE: NallaNudiDatabase? = null

        fun getDatabase(context: Context): NallaNudiDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NallaNudiDatabase::class.java,
                    "nalla_nudi_database"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    populateDatabase(database.termDao())
                }
            }
        }

        suspend fun populateDatabase(termDao: TermDao) {
            val terms = listOf(
                // Science - Physics
                Term(englishTerm = "Gravity", kannadaTerm = "ಗುರುತ್ವಾಕರ್ಷಣೆ", explanation = "ಭೂಮಿ ಎಲ್ಲ ವಸ್ತುಗಳನ್ನು ತನ್ನೆಡೆಗೆ ಸೆಳೆಯುವ ಶಕ್ತಿ", subject = "Science", subSubject = "Physics", exampleSentence = "Gravity pulls the apple down from the tree."),
                Term(englishTerm = "Velocity", kannadaTerm = "ವೇಗ", explanation = "ಒಂದು ವಸ್ತು ನಿರ್ದಿಷ್ಟ ದಿಕ್ಕಿನಲ್ಲಿ ಚಲಿಸುವ ವೇಗ", subject = "Science", subSubject = "Physics", exampleSentence = "The velocity of the car is 60 km/h towards north."),
                Term(englishTerm = "Acceleration", kannadaTerm = "ತ್ವರಣ", explanation = "ವೇಗದಲ್ಲಿ ಆಗುವ ಬದಲಾವಣೆಯ ದರ", subject = "Science", subSubject = "Physics", exampleSentence = "The acceleration of the bike increased suddenly."),
                Term(englishTerm = "Friction", kannadaTerm = "ಘರ್ಷಣೆ", explanation = "ಎರಡು ವಸ್ತುಗಳ ನಡುವೆ ಚಲನೆಯನ್ನು ವಿರೋಧಿಸುವ ಶಕ್ತಿ", subject = "Science", subSubject = "Physics", exampleSentence = "Friction stops the ball from rolling forever."),
                Term(englishTerm = "Inertia", kannadaTerm = "ಜಡತ್ವ", explanation = "ವಸ್ತು ತನ್ನ ಸ್ಥಿತಿಯನ್ನು ಬದಲಾಯಿಸದೇ ಇರಲು ಪ್ರಯತ್ನಿಸುವ ಗುಣ", subject = "Science", subSubject = "Physics", exampleSentence = "Inertia keeps the book at rest on the table."),
                Term(englishTerm = "Momentum", kannadaTerm = "ಆವೇಗ", explanation = "ವಸ್ತುವಿನ ದ್ರವ್ಯರಾಶಿ ಮತ್ತು ವೇಗದ ಗುಣಲಬ್ಧ", subject = "Science", subSubject = "Physics", exampleSentence = "A heavy truck has more momentum than a bicycle."),
                Term(englishTerm = "Energy", kannadaTerm = "ಶಕ್ತಿ", explanation = "ಕೆಲಸ ಮಾಡುವ ಸಾಮರ್ಥ್ಯ", subject = "Science", subSubject = "Physics", exampleSentence = "The sun gives us light and heat energy."),
                Term(englishTerm = "Force", kannadaTerm = "ಬಲ", explanation = "ವಸ್ತುವಿನ ಚಲನೆಯನ್ನು ಬದಲಾಯಿಸುವ ಪ್ರಭಾವ", subject = "Science", subSubject = "Physics", exampleSentence = "We apply force to push the door open."),

                // Science - Chemistry
                Term(englishTerm = "Oxidation", kannadaTerm = "ಆಕ್ಸಿಡೀಕರಣ", explanation = "ಒಂದು ವಸ್ತು ಆಮ್ಲಜನಕದೊಂದಿಗೆ ಸೇರುವ ಕ್ರಿಯೆ", subject = "Science", subSubject = "Chemistry", exampleSentence = "Oxidation causes iron to rust."),
                Term(englishTerm = "Reduction", kannadaTerm = "ಅಪಚಯ", explanation = "ಒಂದು ವಸ್ತು ಆಮ್ಲಜನಕವನ್ನು ಕಳೆದುಕೊಳ್ಳುವ ಕ್ರಿಯೆ", subject = "Science", subSubject = "Chemistry", exampleSentence = "Reduction happens when iron oxide becomes iron."),
                Term(englishTerm = "Acid", kannadaTerm = "ಆಮ್ಲ", explanation = "ಹುಳಿ ರುಚಿ ಹೊಂದಿದ್ದು, ಕೆಂಪು ಲಿಟ್ಮಸ್ ಅನ್ನು ನೀಲಿ ಮಾಡುವ ವಸ್ತು", subject = "Science", subSubject = "Chemistry", exampleSentence = "Lemon juice contains citric acid."),
                Term(englishTerm = "Base", kannadaTerm = "ಪ್ರತ್ಯಾಮ್ಲ", explanation = "ಆಮ್ಲವನ್ನು ತಟಸ್ಥಗೊಳಿಸುವ ವಸ್ತು", subject = "Science", subSubject = "Chemistry", exampleSentence = "Baking soda is a common base."),
                Term(englishTerm = "Catalyst", kannadaTerm = "ವೇಗವರ್ಧಕ", explanation = "ರಾಸಾಯನಿಕ ಕ್ರಿಯೆಯ ವೇಗವನ್ನು ಹೆಚ್ಚಿಸುವ ವಸ್ತು", subject = "Science", subSubject = "Chemistry", exampleSentence = "Manganese dioxide acts as a catalyst in this reaction."),

                // Science - Biology
                Term(englishTerm = "Photosynthesis", kannadaTerm = "ದ್ಯುತಿಸಂಶ್ಲೇಷಣೆ", explanation = "ಸಸ್ಯಗಳು ಸೂರ್ಯನ ಬೆಳಕನ್ನು ಬಳಸಿ ಆಹಾರ ತಯಾರಿಸುವ ಪ್ರಕ್ರಿಯೆ", subject = "Science", subSubject = "Biology", exampleSentence = "Plants use photosynthesis to make their food."),
                Term(englishTerm = "Mitosis", kannadaTerm = "ಸಮವಿಭಜನ", explanation = "ಒಂದು ಜೀವಕೋಶ ಎರಡು ಒಂದೇ ರೀತಿಯ ಜೀವಕೋಶಗಳಾಗಿ ವಿಭಜನೆ", subject = "Science", subSubject = "Biology", exampleSentence = "Mitosis helps the body grow and repair itself."),
                Term(englishTerm = "Osmosis", kannadaTerm = "ಅಭಿಸರಣ", explanation = "ನೀರು ಅರೆಪ್ರವೇಶ್ಯ ಪೊರೆಯ ಮೂಲಕ ಹರಿಯುವ ಪ್ರಕ್ರಿಯೆ", subject = "Science", subSubject = "Biology", exampleSentence = "Water enters plant roots by osmosis."),
                Term(englishTerm = "Chromosome", kannadaTerm = "ವರ್ಣತಂತು", explanation = "ಜೀವಕೋಶದಲ್ಲಿ ಆನುವಂಶಿಕ ಮಾಹಿತಿ ಹೊಂದಿರುವ ರಚನೆ", subject = "Science", subSubject = "Biology", exampleSentence = "Humans have 46 chromosomes in each cell."),
                Term(englishTerm = "Respiration", kannadaTerm = "ಉಸಿರಾಟ", explanation = "ಜೀವಿಗಳು ಆಮ್ಲಜನಕ ಬಳಸಿ ಶಕ್ತಿ ಉತ್ಪಾದಿಸುವ ಪ್ರಕ್ರಿಯೆ", subject = "Science", subSubject = "Biology", exampleSentence = "Respiration converts glucose into energy."),

                // Mathematics
                Term(englishTerm = "Trigonometry", kannadaTerm = "ತ್ರಿಕೋಣಮಿತಿ", explanation = "ತ್ರಿಕೋಣಗಳ ಬದಿ ಮತ್ತು ಕೋನಗಳ ನಡುವಿನ ಸಂಬಂಧದ ಅಧ್ಯಯನ", subject = "Mathematics", subSubject = "Algebra", exampleSentence = "Trigonometry is used to find the height of a mountain."),
                Term(englishTerm = "Quadratic Equation", kannadaTerm = "ವರ್ಗ ಸಮೀಕರಣ", explanation = "ax²+bx+c=0 ರೂಪದ ಸಮೀಕರಣ", subject = "Mathematics", subSubject = "Algebra", exampleSentence = "Solve the quadratic equation to find x."),
                Term(englishTerm = "Polynomial", kannadaTerm = "ಬಹುಪದ", explanation = "ಒಂದು ಅಥವಾ ಹೆಚ್ಚು ಪದಗಳನ್ನು ಹೊಂದಿರುವ ಬೀಜಗಣಿತ ಸಂಖ್ಯೆ", subject = "Mathematics", subSubject = "Algebra", exampleSentence = "x²+3x+2 is a polynomial."),
                Term(englishTerm = "Probability", kannadaTerm = "ಸಂಭಾವ್ಯತೆ", explanation = "ಘಟನೆ ಸಂಭವಿಸುವ ಸಾಧ್ಯತೆಯ ಅಳತೆ", subject = "Mathematics", subSubject = "Statistics", exampleSentence = "The probability of getting heads is 1/2."),
                Term(englishTerm = "Permutation", kannadaTerm = "ಕ್ರಮಜೋಡಣೆ", explanation = "ವಸ್ತುಗಳನ್ನು ನಿರ್ದಿಷ್ಟ ಕ್ರಮದಲ್ಲಿ ಜೋಡಿಸುವ ವಿಧಾನ", subject = "Mathematics", subSubject = "Statistics", exampleSentence = "Permutation counts the number of ways to arrange books."),
                Term(englishTerm = "Combination", kannadaTerm = "ಸಂಯೋಜನೆ", explanation = "ಕ್ರಮವಿಲ್ಲದೆ ವಸ್ತುಗಳನ್ನು ಆಯ್ಕೆ ಮಾಡುವ ವಿಧಾನ", subject = "Mathematics", subSubject = "Statistics", exampleSentence = "Combination is used to select a team from a group."),
                Term(englishTerm = "Theorem", kannadaTerm = "ಪ್ರಮೇಯ", explanation = "ಸಾಬೀತುಪಡಿಸಬಹುದಾದ ಗಣಿತದ ಹೇಳಿಕೆ", subject = "Mathematics", subSubject = "Geometry", exampleSentence = "Pythagoras theorem relates the sides of a right triangle."),
                Term(englishTerm = "Hypotenuse", kannadaTerm = "ಕರ್ಣ", explanation = "ಲಂಬಕೋನ ತ್ರಿಕೋಣದ ಅತಿ ದೊಡ್ಡ ಬದಿ", subject = "Mathematics", subSubject = "Geometry", exampleSentence = "The hypotenuse is always opposite the right angle."),

                // Commerce
                Term(englishTerm = "Inflation", kannadaTerm = "ಹಣದುಬ್ಬರ", explanation = "ಸರಕುಗಳ ಬೆಲೆ ಸಾಮಾನ್ಯವಾಗಿ ಹೆಚ್ಚಾಗುವ ಪ್ರಕ್ರಿಯೆ", subject = "Commerce", subSubject = "Economics", exampleSentence = "Inflation reduces the purchasing power of money."),
                Term(englishTerm = "Depreciation", kannadaTerm = "ಸವಕಳಿ", explanation = "ಆಸ್ತಿಯ ಮೌಲ್ಯ ಕಾಲಕ್ರಮೇಣ ಕಡಿಮೆಯಾಗುವುದು", subject = "Commerce", subSubject = "Accounting", exampleSentence = "The car's value decreases due to depreciation."),
                Term(englishTerm = "Liability", kannadaTerm = "ಹೊಣೆಗಾರಿಕೆ", explanation = "ವ್ಯಕ್ತಿ ಅಥವಾ ಸಂಸ್ಥೆ ಪಾವತಿಸಬೇಕಾದ ಸಾಲ", subject = "Commerce", subSubject = "Accounting", exampleSentence = "Bank loans are a liability for a company."),
                Term(englishTerm = "Asset", kannadaTerm = "ಆಸ್ತಿ", explanation = "ವ್ಯಕ್ತಿ ಅಥವಾ ಸಂಸ್ಥೆ ಹೊಂದಿರುವ ಮೌಲ್ಯಯುತ ವಸ್ತು", subject = "Commerce", subSubject = "Accounting", exampleSentence = "Buildings and machines are assets of a company."),
                Term(englishTerm = "Dividend", kannadaTerm = "ಲಾಭಾಂಶ", explanation = "ಷೇರುದಾರರಿಗೆ ನೀಡುವ ಲಾಭದ ಪಾಲು", subject = "Commerce", subSubject = "Economics", exampleSentence = "The company paid a dividend to its shareholders."),
                // Science - Physics (Extra)
                Term(englishTerm = "Pressure", kannadaTerm = "ಒತ್ತಡ", explanation = "ಪ್ರತಿ ಘಟಕ ವಿಸ್ತೀರ್ಣಕ್ಕೆ ಅನ್ವಯವಾಗುವ ಬಲ", subject = "Science", subSubject = "Physics", exampleSentence = "Water pressure increases with depth."),
                Term(englishTerm = "Density", kannadaTerm = "ಸಾಂದ್ರತೆ", explanation = "ಪ್ರತಿ ಘಟಕ ಗಾತ್ರಕ್ಕೆ ಇರುವ ದ್ರವ್ಯರಾಶಿ", subject = "Science", subSubject = "Physics", exampleSentence = "Iron has more density than wood."),
                Term(englishTerm = "Refraction", kannadaTerm = "ವಕ್ರೀಭವನ", explanation = "ಬೆಳಕು ಒಂದು ಮಾಧ್ಯಮದಿಂದ ಇನ್ನೊಂದಕ್ಕೆ ಹಾದುಹೋಗುವಾಗ ಬಾಗುವ ವಿದ್ಯಮಾನ", subject = "Science", subSubject = "Physics", exampleSentence = "Refraction makes a straw look bent in water."),
                Term(englishTerm = "Reflection", kannadaTerm = "ಪ್ರತಿಫಲನ", explanation = "ಬೆಳಕು ಮೇಲ್ಮೈಗೆ ತಾಗಿ ಹಿಂದಿರುಗುವ ವಿದ್ಯಮಾನ", subject = "Science", subSubject = "Physics", exampleSentence = "We see our face in a mirror due to reflection."),
                Term(englishTerm = "Convex Lens", kannadaTerm = "ಉಬ್ಬು ಮಸೂರ", explanation = "ಮಧ್ಯಭಾಗ ದಪ್ಪವಾಗಿದ್ದು ಬೆಳಕನ್ನು ಒಂದು ಬಿಂದುವಿನಲ್ಲಿ ಕೇಂದ್ರೀಕರಿಸುವ ಮಸೂರ", subject = "Science", subSubject = "Physics", exampleSentence = "A convex lens is used in a magnifying glass."),
                Term(englishTerm = "Concave Lens", kannadaTerm = "ತಗ್ಗು ಮಸೂರ", explanation = "ಮಧ್ಯಭಾಗ ತೆಳುವಾಗಿದ್ದು ಬೆಳಕನ್ನು ಚದುರಿಸುವ ಮಸೂರ", subject = "Science", subSubject = "Physics", exampleSentence = "A concave lens is used to correct short-sightedness."),
                Term(englishTerm = "Electricity", kannadaTerm = "ವಿದ್ಯುತ್", explanation = "ಎಲೆಕ್ಟ್ರಾನ್‌ಗಳ ಹರಿವಿನಿಂದ ಉಂಟಾಗುವ ಶಕ್ತಿ", subject = "Science", subSubject = "Physics", exampleSentence = "Electricity powers our homes and gadgets."),
                Term(englishTerm = "Resistance", kannadaTerm = "ಪ್ರತಿರೋಧ", explanation = "ವಿದ್ಯುತ್ ಹರಿವನ್ನು ವಿರೋಧಿಸುವ ಗುಣ", subject = "Science", subSubject = "Physics", exampleSentence = "Resistance in a wire converts electricity to heat."),
                Term(englishTerm = "Magnetic Field", kannadaTerm = "ಚುಂಬಕ ಕ್ಷೇತ್ರ", explanation = "ಚುಂಬಕದ ಸುತ್ತ ಇರುವ ಪ್ರಭಾವಿ ಪ್ರದೇಶ", subject = "Science", subSubject = "Physics", exampleSentence = "Earth has a magnetic field that protects us."),
                Term(englishTerm = "Wave", kannadaTerm = "ತರಂಗ", explanation = "ಶಕ್ತಿಯನ್ನು ಒಂದು ಸ್ಥಳದಿಂದ ಇನ್ನೊಂದಕ್ಕೆ ಸಾಗಿಸುವ ಅಲೆ", subject = "Science", subSubject = "Physics", exampleSentence = "Sound travels as a wave through air."),
                Term(englishTerm = "Frequency", kannadaTerm = "ಆವೃತ್ತಿ", explanation = "ಒಂದು ಸೆಕೆಂಡಿನಲ್ಲಿ ಸಂಭವಿಸುವ ತರಂಗಗಳ ಸಂಖ್ಯೆ", subject = "Science", subSubject = "Physics", exampleSentence = "High frequency sounds are not heard by humans."),
                Term(englishTerm = "Amplitude", kannadaTerm = "ವ್ಯಾಪ್ತಿ", explanation = "ತರಂಗದ ಗರಿಷ್ಠ ವಿಸ್ಥಾಪನೆ", subject = "Science", subSubject = "Physics", exampleSentence = "Loud sounds have greater amplitude."),

                // Science - Chemistry (Extra)
                Term(englishTerm = "Molecule", kannadaTerm = "ಅಣು", explanation = "ಎರಡು ಅಥವಾ ಹೆಚ್ಚು ಪರಮಾಣುಗಳ ಸಂಯೋಜನೆ", subject = "Science", subSubject = "Chemistry", exampleSentence = "Water molecule has two hydrogen and one oxygen atom."),
                Term(englishTerm = "Atom", kannadaTerm = "ಪರಮಾಣು", explanation = "ವಸ್ತುವಿನ ಅತ್ಯಂತ ಚಿಕ್ಕ ಕಣ", subject = "Science", subSubject = "Chemistry", exampleSentence = "Everything around us is made of atoms."),
                Term(englishTerm = "Element", kannadaTerm = "ಮೂಲ ವಸ್ತು", explanation = "ಒಂದೇ ರೀತಿಯ ಪರಮಾಣುಗಳಿಂದ ಮಾಡಲ್ಪಟ್ಟ ಶುದ್ಧ ವಸ್ತು", subject = "Science", subSubject = "Chemistry", exampleSentence = "Gold and silver are elements."),
                Term(englishTerm = "Compound", kannadaTerm = "ಸಂಯುಕ್ತ", explanation = "ಎರಡು ಅಥವಾ ಹೆಚ್ಚು ಮೂಲ ವಸ್ತುಗಳು ರಾಸಾಯನಿಕವಾಗಿ ಸೇರಿದ ವಸ್ತು", subject = "Science", subSubject = "Chemistry", exampleSentence = "Water is a compound of hydrogen and oxygen."),
                Term(englishTerm = "Mixture", kannadaTerm = "ಮಿಶ್ರಣ", explanation = "ಎರಡು ಅಥವಾ ಹೆಚ್ಚು ವಸ್ತುಗಳು ರಾಸಾಯನಿಕ ಬದಲಾವಣೆ ಇಲ್ಲದೆ ಸೇರಿದ್ದು", subject = "Science", subSubject = "Chemistry", exampleSentence = "Air is a mixture of gases."),
                Term(englishTerm = "Evaporation", kannadaTerm = "ಆವಿಯಾಗುವಿಕೆ", explanation = "ದ್ರವ ವಸ್ತು ಮೇಲ್ಮೈಯಿಂದ ಆವಿಯಾಗಿ ಹೋಗುವ ಪ್ರಕ್ರಿಯೆ", subject = "Science", subSubject = "Chemistry", exampleSentence = "Water evaporates from lakes under sunlight."),
                Term(englishTerm = "Condensation", kannadaTerm = "ಘನೀಕರಣ", explanation = "ಆವಿ ತಂಪಾಗಿ ದ್ರವವಾಗುವ ಪ್ರಕ್ರಿಯೆ", subject = "Science", subSubject = "Chemistry", exampleSentence = "Condensation forms water droplets on a cold glass."),
                Term(englishTerm = "Solvent", kannadaTerm = "ದ್ರಾವಕ", explanation = "ಇತರ ವಸ್ತುಗಳನ್ನು ಕರಗಿಸುವ ದ್ರವ", subject = "Science", subSubject = "Chemistry", exampleSentence = "Water is the most common solvent."),
                Term(englishTerm = "Solute", kannadaTerm = "ದ್ರಾವ್ಯ", explanation = "ದ್ರಾವಕದಲ್ಲಿ ಕರಗುವ ವಸ್ತು", subject = "Science", subSubject = "Chemistry", exampleSentence = "Salt is the solute when dissolved in water."),
                Term(englishTerm = "pH Scale", kannadaTerm = "pH ಮಾಪಕ", explanation = "ದ್ರಾವಣದ ಆಮ್ಲೀಯತೆ ಅಥವಾ ಪ್ರತ್ಯಾಮ್ಲೀಯತೆ ಅಳೆಯುವ ಮಾಪಕ", subject = "Science", subSubject = "Chemistry", exampleSentence = "A pH below 7 means the solution is acidic."),
                Term(englishTerm = "Combustion", kannadaTerm = "ದಹನ", explanation = "ವಸ್ತು ಆಮ್ಲಜನಕದೊಂದಿಗೆ ಸೇರಿ ಶಾಖ ಮತ್ತು ಬೆಳಕು ಉತ್ಪಾದಿಸುವ ಕ್ರಿಯೆ", subject = "Science", subSubject = "Chemistry", exampleSentence = "Combustion of petrol runs our vehicles."),

                // Science - Biology (Extra)
                Term(englishTerm = "Cell", kannadaTerm = "ಜೀವಕೋಶ", explanation = "ಜೀವಿಗಳ ಮೂಲಭೂತ ರಚನಾತ್ಮಕ ಮತ್ತು ಕ್ರಿಯಾತ್ಮಕ ಘಟಕ", subject = "Science", subSubject = "Biology", exampleSentence = "The human body is made of trillions of cells."),
                Term(englishTerm = "Nucleus", kannadaTerm = "ಕೋಶ ಕೇಂದ್ರ", explanation = "ಜೀವಕೋಶದ ನಿಯಂತ್ರಣ ಕೇಂದ್ರ", subject = "Science", subSubject = "Biology", exampleSentence = "The nucleus controls all activities of the cell."),
                Term(englishTerm = "Tissue", kannadaTerm = "ಅಂಗಾಂಶ", explanation = "ಒಂದೇ ರೀತಿಯ ಜೀವಕೋಶಗಳ ಗುಂಪು", subject = "Science", subSubject = "Biology", exampleSentence = "Muscle tissue helps the body move."),
                Term(englishTerm = "Organ", kannadaTerm = "ಅಂಗ", explanation = "ನಿರ್ದಿಷ್ಟ ಕಾರ್ಯ ನಿರ್ವಹಿಸುವ ಅಂಗಾಂಶಗಳ ಗುಂಪು", subject = "Science", subSubject = "Biology", exampleSentence = "The heart is an organ that pumps blood."),
                Term(englishTerm = "Ecosystem", kannadaTerm = "ಪರಿಸರ ವ್ಯವಸ್ಥೆ", explanation = "ಜೀವಿಗಳು ಮತ್ತು ಅವುಗಳ ಪರಿಸರದ ನಡುವಿನ ಪರಸ್ಪರ ಸಂಬಂಧ", subject = "Science", subSubject = "Biology", exampleSentence = "A forest is a complex ecosystem."),
                Term(englishTerm = "Heredity", kannadaTerm = "ಆನುವಂಶಿಕತೆ", explanation = "ಪೋಷಕರಿಂದ ಸಂತಾನಕ್ಕೆ ಗುಣಗಳು ವರ್ಗಾವಣೆಯಾಗುವ ಪ್ರಕ್ರಿಯೆ", subject = "Science", subSubject = "Biology", exampleSentence = "Eye colour is determined by heredity."),
                Term(englishTerm = "Hormone", kannadaTerm = "ಹಾರ್ಮೋನ್", explanation = "ದೇಹದ ಕಾರ್ಯಗಳನ್ನು ನಿಯಂತ್ರಿಸುವ ರಾಸಾಯನಿಕ ಸಂದೇಶಗಳು", subject = "Science", subSubject = "Biology", exampleSentence = "Insulin is a hormone that controls blood sugar."),
                Term(englishTerm = "Vaccination", kannadaTerm = "ಲಸಿಕೆ", explanation = "ರೋಗ ನಿರೋಧಕ ಶಕ್ತಿ ಹೆಚ್ಚಿಸಲು ನೀಡುವ ಔಷಧ", subject = "Science", subSubject = "Biology", exampleSentence = "Vaccination prevents many deadly diseases."),
                Term(englishTerm = "Digestion", kannadaTerm = "ಜೀರ್ಣಕ್ರಿಯೆ", explanation = "ಆಹಾರವನ್ನು ಸರಳ ಅಣುಗಳಾಗಿ ಒಡೆಯುವ ಪ್ರಕ್ರಿಯೆ", subject = "Science", subSubject = "Biology", exampleSentence = "Digestion begins in the mouth."),
                Term(englishTerm = "Phototropism", kannadaTerm = "ಬೆಳಕು ಅನುವರ್ತನ", explanation = "ಸಸ್ಯಗಳು ಬೆಳಕಿನ ಕಡೆಗೆ ಬಾಗುವ ಗುಣ", subject = "Science", subSubject = "Biology", exampleSentence = "Sunflowers show phototropism by facing the sun."),

                // Mathematics (Extra)
                Term(englishTerm = "Integer", kannadaTerm = "ಪೂರ್ಣಾಂಕ", explanation = "ಭಿನ್ನರಾಶಿ ಇಲ್ಲದ ಸಂಖ್ಯೆ (ಧನ, ಋಣ ಅಥವಾ ಶೂನ್ಯ)", subject = "Mathematics", subSubject = "Algebra", exampleSentence = "-3, 0, 5 are all integers."),
                Term(englishTerm = "Fraction", kannadaTerm = "ಭಿನ್ನರಾಶಿ", explanation = "ಒಂದು ಸಂಖ್ಯೆಯ ಭಾಗವನ್ನು ಪ್ರತಿನಿಧಿಸುವ ಅಭಿವ್ಯಕ್ತಿ", subject = "Mathematics", subSubject = "Algebra", exampleSentence = "1/2 is a fraction representing half."),
                Term(englishTerm = "Decimal", kannadaTerm = "ದಶಮಾಂಶ", explanation = "ದಶಮಾಂಶ ಬಿಂದು ಬಳಸಿ ಭಿನ್ನರಾಶಿ ತೋರಿಸುವ ವಿಧಾನ", subject = "Mathematics", subSubject = "Algebra", exampleSentence = "0.5 is the decimal form of 1/2."),
                Term(englishTerm = "Ratio", kannadaTerm = "ಅನುಪಾತ", explanation = "ಎರಡು ಸಂಖ್ಯೆಗಳ ನಡುವಿನ ತುಲನಾತ್ಮಕ ಸಂಬಂಧ", subject = "Mathematics", subSubject = "Algebra", exampleSentence = "The ratio of boys to girls is 3:2."),
                Term(englishTerm = "Percentage", kannadaTerm = "ಶೇಕಡಾ", explanation = "ನೂರಕ್ಕೆ ಎಷ್ಟು ಎಂದು ತೋರಿಸುವ ರೀತಿ", subject = "Mathematics", subSubject = "Algebra", exampleSentence = "She scored 85 percentage in her exam."),
                Term(englishTerm = "Equation", kannadaTerm = "ಸಮೀಕರಣ", explanation = "ಎರಡು ಸಂಖ್ಯೆಗಳು ಅಥವಾ ಅಭಿವ್ಯಕ್ತಿಗಳ ಸಮಾನತೆ ತೋರಿಸುವ ಹೇಳಿಕೆ", subject = "Mathematics", subSubject = "Algebra", exampleSentence = "2x + 3 = 7 is a simple equation."),
                Term(englishTerm = "Variable", kannadaTerm = "ಅಪರಿಚಿತ ರಾಶಿ", explanation = "ಬದಲಾಗಬಹುದಾದ ಮೌಲ್ಯವನ್ನು ಸೂಚಿಸುವ ಅಕ್ಷರ", subject = "Mathematics", subSubject = "Algebra", exampleSentence = "In 2x + 3, x is the variable."),
                Term(englishTerm = "Median", kannadaTerm = "ಮಧ್ಯಮ", explanation = "ಅಂಕಗಳನ್ನು ಕ್ರಮದಲ್ಲಿ ಜೋಡಿಸಿದಾಗ ಮಧ್ಯದಲ್ಲಿ ಬರುವ ಮೌಲ್ಯ", subject = "Mathematics", subSubject = "Statistics", exampleSentence = "The median of 1,2,3,4,5 is 3."),
                Term(englishTerm = "Mean", kannadaTerm = "ಸರಾಸರಿ", explanation = "ಎಲ್ಲ ಅಂಕಗಳ ಮೊತ್ತವನ್ನು ಅಂಕಗಳ ಸಂಖ್ಯೆಯಿಂದ ಭಾಗಿಸಿದ ಮೌಲ್ಯ", subject = "Mathematics", subSubject = "Statistics", exampleSentence = "The mean of 2,4,6 is 4."),
                Term(englishTerm = "Mode", kannadaTerm = "ಮಾದರಿ", explanation = "ಡೇಟಾದಲ್ಲಿ ಹೆಚ್ಚು ಬಾರಿ ಬರುವ ಮೌಲ್ಯ", subject = "Mathematics", subSubject = "Statistics", exampleSentence = "In 1,2,2,3, the mode is 2."),
                Term(englishTerm = "Perimeter", kannadaTerm = "ಪರಿಧಿ", explanation = "ಆಕಾರದ ಎಲ್ಲ ಬದಿಗಳ ಒಟ್ಟು ಉದ್ದ", subject = "Mathematics", subSubject = "Geometry", exampleSentence = "The perimeter of a square with side 4 is 16."),
                Term(englishTerm = "Area", kannadaTerm = "ವಿಸ್ತೀರ್ಣ", explanation = "ಆಕಾರದ ಒಳಗಿನ ಸ್ಥಳದ ಅಳತೆ", subject = "Mathematics", subSubject = "Geometry", exampleSentence = "The area of a rectangle is length times width."),
                Term(englishTerm = "Volume", kannadaTerm = "ಘನಫಲ", explanation = "ತ್ರಿಮಾನ ಆಕಾರ ಆಕ್ರಮಿಸುವ ಸ್ಥಳದ ಅಳತೆ", subject = "Mathematics", subSubject = "Geometry", exampleSentence = "The volume of a cube is side cubed."),
                Term(englishTerm = "Parallel Lines", kannadaTerm = "ಸಮಾಂತರ ರೇಖೆಗಳು", explanation = "ಎಂದಿಗೂ ಛೇದಿಸದ ಒಂದೇ ದಿಕ್ಕಿನ ರೇಖೆಗಳು", subject = "Mathematics", subSubject = "Geometry", exampleSentence = "Railway tracks are parallel lines."),
                Term(englishTerm = "Perpendicular", kannadaTerm = "ಲಂಬ", explanation = "90 ಡಿಗ್ರಿ ಕೋನದಲ್ಲಿ ಛೇದಿಸುವ ರೇಖೆಗಳು", subject = "Mathematics", subSubject = "Geometry", exampleSentence = "The walls of a room are perpendicular to the floor."),
                Term(englishTerm = "Logarithm", kannadaTerm = "ಲಾಗರಿದಮ್", explanation = "ಒಂದು ಸಂಖ್ಯೆಯನ್ನು ಪಡೆಯಲು ಆಧಾರವನ್ನು ಎಷ್ಟು ಬಾರಿ ಗುಣಿಸಬೇಕು ಎಂದು ತೋರಿಸುವ ಮೌಲ್ಯ", subject = "Mathematics", subSubject = "Algebra", exampleSentence = "Log 100 to base 10 is 2."),
                Term(englishTerm = "Coordinate", kannadaTerm = "ನಿರ್ದೇಶಾಂಕ", explanation = "ಗ್ರಾಫ್‌ನಲ್ಲಿ ಬಿಂದುವಿನ ಸ್ಥಾನ ತೋರಿಸುವ ಸಂಖ್ಯೆಗಳ ಜೋಡಿ", subject = "Mathematics", subSubject = "Geometry", exampleSentence = "Point (3,4) has coordinates 3 and 4."),

                // Commerce (Extra)
                Term(englishTerm = "Balance Sheet", kannadaTerm = "ಆರ್ಥಿಕ ಪಟ್ಟಿ", explanation = "ಸಂಸ್ಥೆಯ ಆಸ್ತಿ, ಹೊಣೆ ಮತ್ತು ಬಂಡವಾಳ ತೋರಿಸುವ ಹೇಳಿಕೆ", subject = "Commerce", subSubject = "Accounting", exampleSentence = "The balance sheet shows the financial position of a company."),
                Term(englishTerm = "Profit", kannadaTerm = "ಲಾಭ", explanation = "ಆದಾಯ ವೆಚ್ಚಕ್ಕಿಂತ ಹೆಚ್ಚಾದಾಗ ಉಂಟಾಗುವ ಹೆಚ್ಚುವರಿ", subject = "Commerce", subSubject = "Accounting", exampleSentence = "The company made a profit of 1 lakh this year."),
                Term(englishTerm = "Loss", kannadaTerm = "ನಷ್ಟ", explanation = "ವೆಚ್ಚ ಆದಾಯಕ್ಕಿಂತ ಹೆಚ್ಚಾದಾಗ ಉಂಟಾಗುವ ಕೊರತೆ", subject = "Commerce", subSubject = "Accounting", exampleSentence = "The shop ran at a loss during the lockdown."),
                Term(englishTerm = "Investment", kannadaTerm = "ಹೂಡಿಕೆ", explanation = "ಭವಿಷ್ಯದ ಲಾಭಕ್ಕಾಗಿ ಹಣ ಬಳಸುವ ಕ್ರಿಯೆ", subject = "Commerce", subSubject = "Economics", exampleSentence = "Investing in education gives long-term returns."),
                Term(englishTerm = "Interest", kannadaTerm = "ಬಡ್ಡಿ", explanation = "ಸಾಲ ಅಥವಾ ಠೇವಣಿ ಮೇಲೆ ನೀಡುವ ಅಥವಾ ಪಡೆಯುವ ಹೆಚ್ಚುವರಿ ಹಣ", subject = "Commerce", subSubject = "Economics", exampleSentence = "The bank charges 10% interest on loans."),
                Term(englishTerm = "Tax", kannadaTerm = "ತೆರಿಗೆ", explanation = "ಸರ್ಕಾರಕ್ಕೆ ಕಡ್ಡಾಯವಾಗಿ ಪಾವತಿಸಬೇಕಾದ ಹಣ", subject = "Commerce", subSubject = "Economics", exampleSentence = "We pay income tax on our earnings."),
                Term(englishTerm = "Budget", kannadaTerm = "ಬಜೆಟ್", explanation = "ಆದಾಯ ಮತ್ತು ವೆಚ್ಚದ ಯೋಜಿತ ಹೇಳಿಕೆ", subject = "Commerce", subSubject = "Economics", exampleSentence = "The government presents a budget every year."),
                Term(englishTerm = "Supply", kannadaTerm = "ಪೂರೈಕೆ", explanation = "ಮಾರುಕಟ್ಟೆಯಲ್ಲಿ ಲಭ್ಯವಿರುವ ವಸ್ತು ಅಥವಾ ಸೇವೆಯ ಪ್ರಮಾಣ", subject = "Commerce", subSubject = "Economics", exampleSentence = "When supply increases, prices usually fall."),
                Term(englishTerm = "Demand", kannadaTerm = "ಬೇಡಿಕೆ", explanation = "ಗ್ರಾಹಕರು ಖರೀದಿಸಲು ಇಚ್ಛಿಸುವ ವಸ್ತು ಅಥವಾ ಸೇವೆಯ ಪ್ರಮಾಣ", subject = "Commerce", subSubject = "Economics", exampleSentence = "The demand for smartphones is increasing."),
                Term(englishTerm = "Entrepreneur", kannadaTerm = "ಉದ್ಯಮಿ", explanation = "ಹೊಸ ವ್ಯವಹಾರ ಪ್ರಾರಂಭಿಸುವ ಮತ್ತು ನಿರ್ವಹಿಸುವ ವ್ಯಕ್ತಿ", subject = "Commerce", subSubject = "Economics", exampleSentence = "Ratan Tata is a famous entrepreneur."),
                Term(englishTerm = "Capital", kannadaTerm = "ಬಂಡವಾಳ", explanation = "ವ್ಯವಹಾರ ಪ್ರಾರಂಭಿಸಲು ಬೇಕಾದ ಹಣ ಅಥವಾ ಸಂಪನ್ಮೂಲ", subject = "Commerce", subSubject = "Accounting", exampleSentence = "He invested 5 lakh as capital to start his business."),
                Term(englishTerm = "Revenue", kannadaTerm = "ಆದಾಯ", explanation = "ವ್ಯವಹಾರದಿಂದ ಗಳಿಸುವ ಒಟ್ಟು ಹಣ", subject = "Commerce", subSubject = "Accounting", exampleSentence = "The company's revenue doubled this quarter."),
                Term(englishTerm = "Expenditure", kannadaTerm = "ವೆಚ್ಚ", explanation = "ವ್ಯವಹಾರ ನಡೆಸಲು ಖರ್ಚು ಮಾಡುವ ಹಣ", subject = "Commerce", subSubject = "Accounting", exampleSentence = "Reducing expenditure increases profit."),
            )
            termDao.insertAll(terms)
        }
    }
}