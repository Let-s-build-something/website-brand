package augmy.interactive.com.ui.landing.social_circle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val socialCircleModule = module {
    viewModelOf(::SocialCircleModel)
}

class SocialCircleModel: ViewModel() {

    private val _people = MutableStateFlow(demoPeople)
    private val _categories = MutableStateFlow(listOf(NetworkProximityCategory.Family))

    val categories = _categories.asStateFlow()
    val people = _people.combine(_categories) { people, categories ->
        withContext(Dispatchers.Default) {
            people.filter { item ->
                categories.any { it.range.contains(item.proximity ?: 1f) }
            }
        }
    }

    fun changeCategories(categories: List<NetworkProximityCategory>) {
        viewModelScope.launch(Dispatchers.Default) {
            _categories.value = categories.distinct().sortedByDescending {
                it.range.endInclusive
            }
        }
    }

    companion object {
        private val demoPeople = listOf(
            // Family (5 people)
            NetworkItemIO("Mom", "https://picsum.photos/seed/Mom/80", 10.7f),
            NetworkItemIO("Dad", "https://picsum.photos/seed/Dad/80", 10.3f),
            NetworkItemIO("Sister", "https://picsum.photos/seed/Sister/80", 10.6f),
            NetworkItemIO("Rachel", "https://picsum.photos/seed/Rachel/80", 10.9f),
            NetworkItemIO("Sam", "https://picsum.photos/seed/Sam/80", 10.5f),

            // Peers (10)
            NetworkItemIO("Jordan", "https://picsum.photos/seed/Jordan/80", 9.6f),
            NetworkItemIO("Taylor", "https://picsum.photos/seed/Taylor/80", 9.2f),
            NetworkItemIO("Avery", "https://picsum.photos/seed/Avery/80", 9.8f),
            NetworkItemIO("Jamie", "https://picsum.photos/seed/Jamie/80", 8.9f),
            NetworkItemIO("Morgan", "https://picsum.photos/seed/Morgan/80", 9.1f),
            NetworkItemIO("Riley", "https://picsum.photos/seed/Riley/80", 8.7f),
            NetworkItemIO("Casey", "https://picsum.photos/seed/Casey/80", 8.8f),
            NetworkItemIO("Drew", "https://picsum.photos/seed/Drew/80", 9.4f),
            NetworkItemIO("Sky", "https://picsum.photos/seed/Sky/80", 9.0f),
            NetworkItemIO("Remy", "https://picsum.photos/seed/Remy/80", 9.3f),

            // Community (20)
            NetworkItemIO("Nora", "https://picsum.photos/seed/Nora/80", 7.8f),
            NetworkItemIO("Leo", "https://picsum.photos/seed/Leo/80", 6.5f),
            NetworkItemIO("Isla", "https://picsum.photos/seed/Isla/80", 7.2f),
            NetworkItemIO("Mateo", "https://picsum.photos/seed/Mateo/80", 6.8f),
            NetworkItemIO("Sophie", "https://picsum.photos/seed/Sophie/80", 7.5f),
            NetworkItemIO("Elijah", "https://picsum.photos/seed/Elijah/80", 5.9f),
            NetworkItemIO("Liam", "https://picsum.photos/seed/Liam/80", 6.3f),
            NetworkItemIO("Zoe", "https://picsum.photos/seed/Zoe/80", 6.1f),
            NetworkItemIO("Ella", "https://picsum.photos/seed/Ella/80", 5.2f),
            NetworkItemIO("Maya", "https://picsum.photos/seed/Maya/80", 7.1f),
            NetworkItemIO("Ben", "https://picsum.photos/seed/Ben/80", 7.3f),
            NetworkItemIO("Chloe", "https://picsum.photos/seed/Chloe/80", 6.9f),
            NetworkItemIO("Aria", "https://picsum.photos/seed/Aria/80", 5.6f),
            NetworkItemIO("Owen", "https://picsum.photos/seed/Owen/80", 6.0f),
            NetworkItemIO("Kai", "https://picsum.photos/seed/Kai/80", 7.0f),
            NetworkItemIO("Amira", "https://picsum.photos/seed/Amira/80", 5.5f),
            NetworkItemIO("Noah", "https://picsum.photos/seed/Noah/80", 5.8f),
            NetworkItemIO("Sasha", "https://picsum.photos/seed/Sasha/80", 6.6f),
            NetworkItemIO("Jules", "https://picsum.photos/seed/Jules/80", 6.7f),
            NetworkItemIO("Lila", "https://picsum.photos/seed/Lila/80", 5.7f),

            // Contacts (30)
            NetworkItemIO("Tara", "https://picsum.photos/seed/Tara/80", 4.5f),
            NetworkItemIO("Dean", "https://picsum.photos/seed/Dean/80", 4.2f),
            NetworkItemIO("Milo", "https://picsum.photos/seed/Milo/80", 4.8f),
            NetworkItemIO("Luna", "https://picsum.photos/seed/Luna/80", 4.0f),
            NetworkItemIO("Nico", "https://picsum.photos/seed/Nico/80", 3.9f),
            NetworkItemIO("Gia", "https://picsum.photos/seed/Gia/80", 3.5f),
            NetworkItemIO("Finn", "https://picsum.photos/seed/Finn/80", 3.2f),
            NetworkItemIO("Cleo", "https://picsum.photos/seed/Cleo/80", 3.7f),
            NetworkItemIO("Reed", "https://picsum.photos/seed/Reed/80", 3.3f),
            NetworkItemIO("Sage", "https://picsum.photos/seed/Sage/80", 4.3f),
            NetworkItemIO("Lars", "https://picsum.photos/seed/Lars/80", 4.6f),
            NetworkItemIO("Ivy", "https://picsum.photos/seed/Ivy/80", 3.8f),
            NetworkItemIO("Beau", "https://picsum.photos/seed/Beau/80", 3.4f),
            NetworkItemIO("Gabe", "https://picsum.photos/seed/Gabe/80", 4.4f),
            NetworkItemIO("Nina", "https://picsum.photos/seed/Nina/80", 4.7f),
            NetworkItemIO("Alexis", "https://picsum.photos/seed/Alexis/80", 4.1f),
            NetworkItemIO("Theo", "https://picsum.photos/seed/Theo/80", 4.9f),
            NetworkItemIO("Freya", "https://picsum.photos/seed/Freya/80", 4.2f),
            NetworkItemIO("Zion", "https://picsum.photos/seed/Zion/80", 3.6f),
            NetworkItemIO("Callie", "https://picsum.photos/seed/Callie/80", 4.8f),
            NetworkItemIO("Ravi", "https://picsum.photos/seed/Ravi/80", 3.1f),
            NetworkItemIO("Hazel", "https://picsum.photos/seed/Hazel/80", 4.0f),
            NetworkItemIO("Yara", "https://picsum.photos/seed/Yara/80", 4.3f),
            NetworkItemIO("Tobias", "https://picsum.photos/seed/Tobias/80", 3.0f),
            NetworkItemIO("Rowan", "https://picsum.photos/seed/Rowan/80", 4.6f),
            NetworkItemIO("Indira", "https://picsum.photos/seed/Indira/80", 4.1f),
            NetworkItemIO("Mina", "https://picsum.photos/seed/Mina/80", 3.6f),
            NetworkItemIO("Koa", "https://picsum.photos/seed/Koa/80", 4.7f),
            NetworkItemIO("Elliot", "https://picsum.photos/seed/Elliot/80", 3.9f),
            NetworkItemIO("Anya", "https://picsum.photos/seed/Anya/80", 3.8f),

            // Public (35+)
            NetworkItemIO("Bus Driver", "https://picsum.photos/seed/Bus Driver/80", 2.1f),
            NetworkItemIO("Barista", "https://picsum.photos/seed/Barista/80", 2.3f),
            NetworkItemIO("Dog Park Acquaintance", "https://picsum.photos/seed/Dog Park Acquaintance/80", 2.8f),
            NetworkItemIO("Old Classmate", "https://picsum.photos/seed/Old Classmate/80", 2.5f),
            NetworkItemIO("Dry Cleaner", "https://picsum.photos/seed/Dry Cleaner/80", 2.0f),
            NetworkItemIO("Trainer", "https://picsum.photos/seed/Trainer/80", 2.6f),
            NetworkItemIO("Librarian", "https://picsum.photos/seed/Librarian/80", 1.9f),
            NetworkItemIO("Book Club Member", "https://picsum.photos/seed/Book Club Member/80", 2.4f),
            NetworkItemIO("Event Host", "https://picsum.photos/seed/host/80", 2.7f),
            NetworkItemIO("Neighbor’s Cousin", "https://picsum.photos/seed/NeighbossCousin/80", 1.8f),
            NetworkItemIO("Yoga Classmate", "https://picsum.photos/seed/Yoga Classmate/80", 2.2f),
            NetworkItemIO("Delivery Driver", "https://picsum.photos/seed/Delivery Driver/80", 1.7f),
            NetworkItemIO("Workshop Attendee", "https://picsum.photos/seed/Workshop Attendee/80", 2.9f),
            NetworkItemIO("Museum Guide", "https://picsum.photos/seed/Museum Guide/80", 1.6f),
            NetworkItemIO("Taxi Driver", "https://picsum.photos/seed/Taxi Driver/80", 1.5f),
            NetworkItemIO("Neighbor’s Cousin", "https://picsum.photos/seed/Neighborsc/80", 1.8f),
            NetworkItemIO("Yoga Classmate", "https://picsum.photos/seed/YogasClassmate/80", 2.2f),
            NetworkItemIO("Delivery Driver", "https://picsum.photos/seed/Delavery Driver/80", 1.7f),
            NetworkItemIO("Workshop Attendee", "https://picsum.photos/seed/Wzrkshop Attendee/80", 2.9f),
            NetworkItemIO("Museum Guide", "https://picsum.photos/seed/MuseumxGuide/80", 1.6f),
            NetworkItemIO("Taxi Driver", "https://picsum.photos/seed/TaxivDriver/80", 1.5f),
            NetworkItemIO("Festival Vendor", "https://picsum.photos/seed/FestivalpVendor/80", 2.3f),
            NetworkItemIO("Public Speaker", "https://picsum.photos/seed/speakerp/80", 2.6f),
            NetworkItemIO("Security Guard", "https://picsum.photos/seed/Security Guard/80", 2.4f),
            NetworkItemIO("Museum Attendant", "https://picsum.photos/seed/Museum Attendant/80", 2.0f),
            NetworkItemIO("Gardener", "https://picsum.photos/seed/Gardener/80", 1.9f),
            NetworkItemIO("Art Teacher", "https://picsum.photos/seed/artt/80", 2.1f),
            NetworkItemIO("Mail Carrier", "https://picsum.photos/seed/Mail Carrier/80", 1.8f),
            NetworkItemIO("Clerk", "https://picsum.photos/seed/clerk/80", 2.2f),
            NetworkItemIO("Dog Walker", "https://picsum.photos/seed/Dog Walker/80", 2.5f),
            NetworkItemIO("Window Cleaner", "https://picsum.photos/seed/Window Cleaner/80", 1.6f),
            NetworkItemIO("Tour Guide", "https://picsum.photos/seed/Tour Guide/80", 2.9f),
            NetworkItemIO("Bar Patron", "https://picsum.photos/seed/Bar Patron/80", 2.8f),
            NetworkItemIO("Subway Musician", "https://picsum.photos/seed/Subway Musician/80", 1.7f),
            NetworkItemIO("Park Ranger", "https://picsum.photos/seed/Park Ranger/80", 1.5f),
            NetworkItemIO("Cashier", "https://picsum.photos/seed/casier/80", 2.3f),
            NetworkItemIO("Theo", "https://picsum.photos/seed/Txcheo/80", 1.9f),
            NetworkItemIO("Freya", "https://picsum.photos/seed/Fresya/80", 1.2f),
            NetworkItemIO("Zion", "https://picsum.photos/seed/Zioqn/80", 1.6f),
            NetworkItemIO("Callie", "https://picsum.photos/seed/Calxlie/80", 1.8f),
            NetworkItemIO("Ravi", "https://picsum.photos/seed/Ravia/80", 1.1f),
            NetworkItemIO("Hazel", "https://picsum.photos/seed/Hazzel/80", 2.0f),
            NetworkItemIO("Taxi Dispatcher", "https://picsum.photos/seed/taxidis/80", 2.1f),
            NetworkItemIO("Food Delivery Rider", "https://picsum.photos/seed/Food Delivery Rider/80", 1.8f),
            NetworkItemIO("Bystander", "https://picsum.photos/seed/Bystander/80", 2.2f),
            NetworkItemIO("Receptionist", "https://picsum.photos/seed/Receptionist/80", 2.7f),
            NetworkItemIO("Firefighter", "https://picsum.photos/seed/firefi/80", 2.6f),
            NetworkItemIO("EMT", "https://picsum.photos/seed/EMT/80", 2.0f)
        )
    }
}