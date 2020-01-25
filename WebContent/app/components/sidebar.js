Vue.component("side-bar",{

    template : `
        
        <nav class="col-md-2 d-none d-md-block bg-light sidebar">
        <div class="sidebar-sticky">
        <ul class="nav flex-column">
            <li class="nav-item">
            <a class="nav-link active" href="#/homepage">
                <span data-feather="home"></span>
                Home <span class="sr-only">(current)</span>
            </a>
            </li>
            <li class="nav-item">
            <a class="nav-link" href="#/users">
                <span data-feather="file"></span>
                Users
            </a>
            </li>
            <li class="nav-item">
            <a class="nav-link" href="#/organizations">
                <span data-feather="shopping-cart"></span>
                Organizations
            </a>
            </li>
            <li class="nav-item">
            <a class="nav-link" href="#/categories">
                <span data-feather="categories"></span>
                Categories
            </a>
            </li>
            <li class="nav-item">
            <a class="nav-link" href="#/drives">
                <span data-feather="bar-chart-2"></span>
                Drives
            </a>
            </li>
            <li class="nav-item">
            <a class="nav-link" href="#/profile">
                <span data-feather="layers"></span>
                Edit profile
            </a>
            </li>
        </ul>
        </ul>
        </div>
    </nav>
    `

})